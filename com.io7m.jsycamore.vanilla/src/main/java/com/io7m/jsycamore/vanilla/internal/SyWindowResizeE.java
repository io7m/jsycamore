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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyButtonReadableType;
import com.io7m.jsycamore.api.events.SyEventConsumed;
import com.io7m.jsycamore.api.events.SyEventInputType;
import com.io7m.jsycamore.api.keyboard.SyKeyEventType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.mouse.SyMouseEventType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.List;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_CONSUMED;
import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.BUTTON;
import static com.io7m.jsycamore.api.windows.SyWindowDecorationComponent.WINDOW_RESIZE_E;

/**
 * An east resize button.
 */

public final class SyWindowResizeE
  extends SyWindowComponent
  implements SyButtonReadableType
{
  private boolean pressed;
  private PAreaSizeI<SySpaceViewportType> windowStartSize;

  SyWindowResizeE(final SyScreenType screen)
  {
    super(screen, WINDOW_RESIZE_E, List.of());
  }

  @Override
  protected SyEventConsumed onEventInput(
    final SyEventInputType event)
  {
    return switch (event) {
      case final SyMouseEventType e -> {
        yield this.onMouseEvent(e);
      }
      case final SyKeyEventType e -> EVENT_NOT_CONSUMED;
    };
  }

  private SyEventConsumed onMouseEvent(
    final SyMouseEventType event)
  {
    if (event instanceof final SyMouseEventOnPressed onPressed) {
      return switch (onPressed.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.pressed = true;

          final var window =
            this.window().orElseThrow(UnreachableCodeException::new);

          this.windowStartSize = window.size().get();
          yield EVENT_CONSUMED;
        }
        case MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_MIDDLE -> EVENT_NOT_CONSUMED;
      };
    }

    if (event instanceof final SyMouseEventOnReleased onReleased) {
      return switch (onReleased.button()) {
        case MOUSE_BUTTON_LEFT -> {
          this.pressed = false;
          yield EVENT_CONSUMED;
        }
        case MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_MIDDLE -> EVENT_NOT_CONSUMED;
      };
    }

    if (event instanceof final SyMouseEventOnHeld onHeld) {
      return switch (onHeld.button()) {
        case MOUSE_BUTTON_LEFT -> {
          final var deltaX =
            onHeld.mousePositionNow().x() - onHeld.mousePositionFirst().x();

          final var newSize =
            PAreaSizeI.<SySpaceViewportType>of(
              this.windowStartSize.sizeX() + deltaX,
              this.windowStartSize.sizeY()
            );

          final var window =
            this.window().orElseThrow(UnreachableCodeException::new);

          window.setSize(newSize);
          yield EVENT_CONSUMED;
        }
        case MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_MIDDLE -> EVENT_NOT_CONSUMED;
      };
    }

    return EVENT_NOT_CONSUMED;
  }

  @Override
  public List<SyThemeClassNameType> themeClassesDefaultForComponent()
  {
    return List.of(SyThemeClassNameStandard.WINDOW_RESIZE_E, BUTTON);
  }

  @Override
  public boolean isPressed()
  {
    return this.pressed;
  }
}
