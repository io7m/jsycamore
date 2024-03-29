/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.tests;

import com.io7m.jsycamore.api.components.SyButtonType;
import com.io7m.jsycamore.api.components.SyButtonWithTextType;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnHeld;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnNoLongerOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnOver;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnPressed;
import com.io7m.jsycamore.api.mouse.SyMouseEventOnReleased;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameCustom;
import com.io7m.jsycamore.api.windows.SyWindowClosed;
import com.io7m.jsycamore.api.windows.SyWindowID;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.io7m.jsycamore.api.events.SyEventConsumed.EVENT_NOT_CONSUMED;
import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_LEFT;
import static com.io7m.jsycamore.api.mouse.SyMouseButton.MOUSE_BUTTON_RIGHT;
import static com.io7m.jsycamore.api.text.SyText.text;
import static com.io7m.jsycamore.components.standard.buttons.SyButton.button;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyButtonTest extends SyComponentContract<SyButtonType>
{
  private static final PVector2I<SySpaceViewportType> Z =
    PVectors2I.zero();
  private static final PVector2I<SySpaceViewportType> ELSEWHERE =
    PVector2I.of(8192, 8192);

  private int clicks;

  @BeforeEach
  public void buttonSetup()
  {
    this.clicks = 0;
  }

  /**
   * Pressing a button works.
   */

  @Test
  public void testButtonPressRelease()
  {
    final var c = this.newComponent();
    c.setOnClickListener(() -> this.clicks++);

    c.eventSend(new SyMouseEventOnPressed(Z, MOUSE_BUTTON_LEFT, c));
    assertTrue(c.isPressed());
    assertEquals(0, this.clicks);

    c.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, c));
    assertFalse(c.isPressed());
    assertEquals(1, this.clicks);

    c.setOnClickListener(() -> {
    });

    c.eventSend(new SyMouseEventOnPressed(Z, MOUSE_BUTTON_LEFT, c));
    assertTrue(c.isPressed());
    assertEquals(1, this.clicks);

    c.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, c));
    assertFalse(c.isPressed());
    assertEquals(1, this.clicks);
  }

  /**
   * Pressing a button works.
   */

  @Test
  public void testButtonPressReleaseConvenience()
  {
    final var c =
      button(this.screen(), () -> this.clicks++);

    c.eventSend(new SyMouseEventOnPressed(Z, MOUSE_BUTTON_LEFT, c));
    assertTrue(c.isPressed());
    assertEquals(0, this.clicks);

    c.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, c));
    assertFalse(c.isPressed());
    assertEquals(1, this.clicks);

    c.setOnClickListener(() -> {
    });

    c.eventSend(new SyMouseEventOnPressed(Z, MOUSE_BUTTON_LEFT, c));
    assertTrue(c.isPressed());
    assertEquals(1, this.clicks);

    c.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_LEFT, c));
    assertFalse(c.isPressed());
    assertEquals(1, this.clicks);
  }

  /**
   * Pressing a button using the wrong mouse button does nothing.
   */

  @Test
  public void testButtonPressReleaseRight()
  {
    final var c = this.newComponent();
    c.setOnClickListener(() -> this.clicks++);

    c.eventSend(new SyMouseEventOnPressed(Z, MOUSE_BUTTON_RIGHT, c));
    assertFalse(c.isPressed());
    assertEquals(0, this.clicks);

    c.eventSend(new SyMouseEventOnReleased(Z, MOUSE_BUTTON_RIGHT, c));
    assertFalse(c.isPressed());
    assertEquals(0, this.clicks);
  }

  /**
   * Pressing a button and then releasing the button with the cursor elsewhere
   * works.
   */

  @Test
  public void testButtonPressReleaseNotOver()
  {
    final var c = this.newComponent();
    c.setOnClickListener(() -> this.clicks++);

    c.eventSend(new SyMouseEventOnPressed(Z, MOUSE_BUTTON_LEFT, c));
    assertTrue(c.isPressed());
    assertEquals(0, this.clicks);

    c.eventSend(new SyMouseEventOnNoLongerOver());
    c.eventSend(new SyMouseEventOnReleased(ELSEWHERE, MOUSE_BUTTON_LEFT, c));
    assertFalse(c.isPressed());
    assertEquals(0, this.clicks);
  }

  /**
   * Pressing a button and then releasing the button with the cursor elsewhere
   * works.
   */

  @Test
  public void testButtonPressReleaseNotOverDrag()
  {
    final var c = this.newComponent();
    c.setOnClickListener(() -> this.clicks++);

    c.eventSend(new SyMouseEventOnPressed(Z, MOUSE_BUTTON_LEFT, c));
    assertTrue(c.isPressed());
    assertEquals(0, this.clicks);

    c.eventSend(new SyMouseEventOnHeld(Z, ELSEWHERE, MOUSE_BUTTON_LEFT, c));
    c.eventSend(new SyMouseEventOnReleased(ELSEWHERE, MOUSE_BUTTON_LEFT, c));
    assertFalse(c.isPressed());
    assertEquals(0, this.clicks);
  }

  /**
   * Pressing a button and then dragging the mouse works.
   */

  @Test
  public void testButtonScreenPressDrag()
  {
    final var c = this.newComponent();
    this.windowContentArea().childAdd(c);
    this.window().layout(this.layoutContext);
    this.screen().mouseMoved(Z);

    c.setOnClickListener(() -> this.clicks++);

    this.screen().mouseDown(Z, MOUSE_BUTTON_LEFT);
    assertTrue(c.isPressed());
    assertTrue(c.isMouseOver());
    assertEquals(0, this.clicks);

    this.screen().mouseMoved(Z);
    assertTrue(c.isPressed());
    assertTrue(c.isMouseOver());
    assertEquals(0, this.clicks);

    this.screen().mouseUp(Z, MOUSE_BUTTON_LEFT);
    assertFalse(c.isPressed());
    assertTrue(c.isMouseOver());
    assertEquals(1, this.clicks);
  }

  /**
   * Moving the cursor over the button works.
   */

  @Test
  public void testButtonOver()
  {
    final var c = this.newComponent();
    c.setOnClickListener(() -> this.clicks++);
    assertFalse(c.isMouseOver());

    c.eventSend(new SyMouseEventOnOver(Z, c));
    assertFalse(c.isPressed());
    assertTrue(c.isMouseOver());
    assertEquals(0, this.clicks);

    c.eventSend(new SyMouseEventOnNoLongerOver());
    assertFalse(c.isMouseOver());
  }

  /**
   * Setting button texts works.
   */

  @Test
  public void testButtonText()
  {
    final var c = this.newComponent();
    c.setText(text("A"));
    assertEquals("A", c.text().get().value());
    c.text().set(text("B"));
    assertEquals("B", c.text().get().value());
    c.setText(text("C"));
    assertEquals("C", c.text().get().value());
  }

  /**
   * Setting the initial button text works.
   */

  @Test
  public void testButtonTextInitial()
  {
    final var c = button(this.screen(), text("Z"));
    assertEquals("Z", c.text().get().value());
  }

  /**
   * Setting the initial button text works (again).
   */

  @Test
  public void testButtonTextInitialExtras()
  {
    final var c =
      button(this.screen(), List.of(new SyThemeClassNameCustom("Q", "Q")), text("Z"));
    assertEquals("Z", c.text().get().value());
    assertEquals("Q", c.themeClassesInPreferenceOrder().get(0).className());
  }

  @Override
  protected SyButtonWithTextType newComponent()
  {
    return button(this.screen());
  }
}
