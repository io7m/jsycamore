/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jsycamore.api.components.SyButtonType;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * The base type of window button components.
 */

public abstract class SyWindowButtonComponent
  extends SyWindowComponent implements SyButtonType
{
  private boolean pressed;
  private Runnable listener;

  protected SyWindowButtonComponent(
    final SyWindowDecorationComponent inSemantic)
  {
    super(inSemantic);
    this.listener = () -> {
    };
  }

  @Override
  public final boolean isPressed()
  {
    return this.pressed;
  }

  @Override
  protected final boolean onEvent(
    final SyEventType event)
  {
    if (event instanceof SyMouseEventType mouseEvent) {
      return this.onMouseEvent(mouseEvent);
    }
    return false;
  }

  private boolean onMouseEvent(
    final SyMouseEventType event)
  {
    /*
     * Track "over" state. The cursor must be over a button to click it.
     */

    if (event instanceof SyMouseEventOnOver) {
      this.setMouseOver(true);
      return true;
    }
    if (event instanceof SyMouseEventOnNoLongerOver) {
      this.setMouseOver(false);
      return true;
    }

    /*
     * If the mouse is pressed, start tracking the "pressed" state.
     */

    if (event instanceof SyMouseEventOnPressed onPressed) {
      this.setMouseOver(true);
      return switch (onPressed.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.pressed = true;
          yield true;
        }
        case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
          yield false;
        }
      };
    }

    /*
     * Buttons should only be triggered if the mouse button is released when
     * the cursor is still over the button.
     */

    if (event instanceof SyMouseEventOnHeld onHeld) {
      return switch (onHeld.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.window()
            .flatMap(window -> {
              return window.componentForViewportPosition(onHeld.mousePositionNow());
            })
            .flatMap(component -> {
              this.setMouseOver(Objects.equals(component, this));
              return Optional.empty();
            });
          yield true;
        }
        case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
          yield false;
        }
      };
    }

    if (event instanceof SyMouseEventOnReleased onReleased) {
      return switch (onReleased.button()) {
        case MOUSE_BUTTON_LEFT -> {

          /*
           * Only trigger the button if the cursor is still over the button
           * when the mouse button is released.
           */

          if (this.pressed && this.isMouseOver()) {
            try {
              this.onClicked();
            } finally {

              /*
               * Both "pressed" and "over" are cancelled here because if the
               * button is moved for any reason by the result of pressing the
               * button, there will not be a "mouse no longer" over event
               * delivered to the button.
               */

              this.pressed = false;
              this.setMouseOver(false);
            }
          }

          this.pressed = false;
          yield true;
        }

        case MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT -> {
          yield false;
        }
      };
    }

    return false;
  }

  protected abstract void onClicked();


  @Override
  public final void setOnClickListener(
    final Runnable runnable)
  {
    this.listener = Objects.requireNonNull(runnable, "runnable");
  }

  @Override
  public final void removeOnClickListener()
  {
    this.listener = () -> {
    };
  }
}
