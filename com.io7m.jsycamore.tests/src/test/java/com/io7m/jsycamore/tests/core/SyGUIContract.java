/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jsycamore.tests.core;

import com.io7m.jranges.RangeCheckException;
import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.SyGUIType;
import com.io7m.jsycamore.api.SyMouseButton;
import com.io7m.jsycamore.api.components.SyComponentType;
import com.io7m.jsycamore.api.components.SyLabelReadableType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.windows.SyWindowContentPaneType;
import com.io7m.jsycamore.api.windows.SyWindowFrameType;
import com.io7m.jsycamore.api.windows.SyWindowTitleBarType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.themes.fenestra.SyThemeFenestra;
import com.io7m.jsycamore.themes.motive.SyThemeMotive;
import com.io7m.jsycamore.themes.stride.SyThemeStride;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Optional;

public abstract class SyGUIContract
{
  @Rule public ExpectedException expected = ExpectedException.none();

  protected abstract SyGUIType create(String name);

  protected abstract SyGUIType createWithTheme(
    String name,
    SyTheme theme);

  @Test
  public final void testCreate()
  {
    final SyGUIType g = this.create("main");
    Assert.assertEquals("main", g.name());
    Assert.assertEquals(SyThemeMotive.builder().build(), g.theme());
    Assert.assertTrue(g.windowsOpenOrdered().isEmpty());
  }

  @Test
  public final void testCreateWithTheme()
  {
    final SyTheme theme = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", theme);
    Assert.assertEquals("main", g.name());
    Assert.assertEquals(theme, g.theme());
    Assert.assertTrue(g.windowsOpenOrdered().isEmpty());
  }

  @Test
  public final void testCreateWindowNegativeWidth()
  {
    final SyGUIType g = this.create("main");
    this.expected.expect(RangeCheckException.class);
    g.windowCreate(-1, 200, "title");
  }

  @Test
  public final void testCreateWindowNegativeHeight()
  {
    final SyGUIType g = this.create("main");
    this.expected.expect(RangeCheckException.class);
    g.windowCreate(100, -1, "title");
  }

  @Test
  public final void testWindowFocusedWrongGUI()
  {
    final SyGUIType g0 = this.create("main");
    final SyGUIType g1 = this.create("other");

    final SyWindowType w0 = g0.windowCreate(100, 100, "title");
    this.expected.expect(IllegalArgumentException.class);
    g1.windowIsFocused(w0);
  }

  @Test
  public final void testCreateWindow()
  {
    final SyGUIType g = this.create("main");

    final SyWindowType w = g.windowCreate(640, 480, "Window 0");
    Assert.assertEquals(g.theme(), w.theme());
    Assert.assertEquals("Window 0", w.titleBar().text());

    final List<SyWindowType> windows = g.windowsOpenOrdered();
    Assert.assertEquals(1L, (long) windows.size());
    Assert.assertEquals(w, windows.get(0));
    Assert.assertTrue(g.windowIsFocused(w));
    Assert.assertTrue(w.isFocused());

    final PAreaI<SySpaceViewportType> window_box = w.box();

    Assert.assertEquals(0L, (long) window_box.minimumX());
    Assert.assertEquals(0L, (long) window_box.minimumY());
    Assert.assertEquals(640L, (long) window_box.sizeX());
    Assert.assertEquals(480L, (long) window_box.sizeY());
  }

  @Test
  public final void testWindowFocus()
  {
    final SyGUIType g = this.create("main");
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");
    final SyWindowType w1 = g.windowCreate(640, 480, "Window 1");

    Assert.assertFalse(w0.isFocused());
    Assert.assertTrue(w1.isFocused());
    Assert.assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w1));
    Assert.assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w0));

    g.windowFocus(w0);

    Assert.assertTrue(w0.isFocused());
    Assert.assertFalse(w1.isFocused());
    Assert.assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w0));
    Assert.assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w1));

    g.windowFocus(w1);

    Assert.assertFalse(w0.isFocused());
    Assert.assertTrue(w1.isFocused());
    Assert.assertEquals(0L, (long) g.windowsOpenOrdered().indexOf(w1));
    Assert.assertEquals(1L, (long) g.windowsOpenOrdered().indexOf(w0));
  }

  @Test
  public final void testWindowThemeChange()
  {
    final SyTheme theme = SyThemeFenestra.builder().build();
    final SyThemeType theme_default = SyThemeMotive.builder().build();

    final SyGUIType g = this.create("main");
    final SyWindowType w0 = g.windowCreate(640, 480, "Main 0");

    Assert.assertEquals(theme_default, g.theme());
    Assert.assertEquals(theme_default, w0.theme());

    g.setTheme(theme);

    Assert.assertEquals(theme, g.theme());
    Assert.assertEquals(theme, w0.theme());
  }

  @Test
  public final void testWindowThemeChangeOpenClosed()
  {
    final SyTheme theme = SyThemeFenestra.builder().build();
    final SyThemeType theme_default = SyThemeMotive.builder().build();

    final SyGUIType g = this.create("main");
    final SyWindowType w0 = g.windowCreate(640, 480, "Main 0");

    Assert.assertEquals(theme_default, g.theme());
    Assert.assertEquals(theme_default, w0.theme());

    g.windowClose(w0);
    g.setTheme(theme);

    Assert.assertEquals(theme, g.theme());
    Assert.assertEquals(theme, w0.theme());
  }

  @Test
  public final void testWindowOpenCloseMulti()
  {
    final SyGUIType g = this.create("main");
    final SyWindowType w0 = g.windowCreate(640, 480, "Main 0");
    final SyWindowType w1 = g.windowCreate(640, 480, "Main 1");

    Assert.assertTrue(w0.isOpen());
    Assert.assertFalse(w0.isFocused());
    Assert.assertTrue(w1.isOpen());
    Assert.assertTrue(w1.isFocused());

    g.windowClose(w1);

    Assert.assertTrue(w0.isOpen());
    Assert.assertTrue(w0.isFocused());
    Assert.assertFalse(w1.isOpen());
    Assert.assertFalse(w1.isFocused());

    g.windowOpen(w1);

    Assert.assertTrue(w0.isOpen());
    Assert.assertFalse(w0.isFocused());
    Assert.assertTrue(w1.isOpen());
    Assert.assertTrue(w1.isFocused());

    g.windowClose(w0);

    Assert.assertFalse(w0.isOpen());
    Assert.assertFalse(w0.isFocused());
    Assert.assertTrue(w1.isOpen());
    Assert.assertTrue(w1.isFocused());

    g.windowClose(w1);

    Assert.assertFalse(w0.isOpen());
    Assert.assertFalse(w0.isFocused());
    Assert.assertFalse(w1.isOpen());
    Assert.assertFalse(w1.isFocused());

    g.windowOpen(w0);

    Assert.assertTrue(w0.isOpen());
    Assert.assertTrue(w0.isFocused());
    Assert.assertFalse(w1.isOpen());
    Assert.assertFalse(w1.isFocused());

    g.windowOpen(w1);

    Assert.assertTrue(w0.isOpen());
    Assert.assertFalse(w0.isFocused());
    Assert.assertTrue(w1.isOpen());
    Assert.assertTrue(w1.isFocused());
  }

  @Test
  public final void testWindowFocusMulti()
  {
    final SyGUIType g =
      this.createWithTheme("main", SyThemeStride.builder().build());

    final SyWindowType w0 = g.windowCreate(320, 240, "Window 0");
    final SyWindowType w1 = g.windowCreate(320, 240, "Window 1");
    w1.setBox(PAreasI.create(320, 0, 320, 240));

    Assert.assertFalse(w0.isFocused());
    Assert.assertTrue(w1.isFocused());

    {
      g.onMouseDown(
        PVector2I.of(2, 2), SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertTrue(w0.isFocused());
      Assert.assertFalse(w1.isFocused());
    }

    {
      g.onMouseDown(
        PVector2I.of(320 + 2, 2), SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertTrue(w1.isFocused());
      Assert.assertFalse(w0.isFocused());
    }
  }

  @Test
  public final void testWindowMouseOverFrame()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(2, 2));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowFrameType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(640 + 2, 480 + 2));
      Assert.assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverContentPane()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(32, 32));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowContentPaneType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(640 + 32, 480 + 32));
      Assert.assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverMulti()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(320, 10));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyLabelReadableType);
      Assert.assertTrue(cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(325, 10));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyLabelReadableType);
      Assert.assertTrue(cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(320, 100));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowContentPaneType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(325, 100));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(cc instanceof SyWindowContentPaneType);
    }
  }

  @Test
  public final void testWindowMouseOverTitlebar()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(320, 10));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(
        cc instanceof SyLabelReadableType);
      Assert.assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(640 + 10, 480 + 10));
      Assert.assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverTitlebarOffset()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");
    w0.setBox(PAreasI.moveAbsolute(w0.box(), 32, 32));

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(32 + 320 + 10, 32 + 10));
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(
        cc instanceof SyLabelReadableType);
      Assert.assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseMoved(PVector2I.of(32 + 640 + 10, 32 + 480 + 10));
      Assert.assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverTitlebarDragLeftButton()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseDown(
          PVector2I.of(320, 10),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(
        cc instanceof SyLabelReadableType);
      Assert.assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    g.onMouseMoved(PVector2I.of(320 + 15, 20));

    {
      final Optional<SyComponentType> c =
        g.onMouseUp(
          PVector2I.of(320 + 15, 20),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(
        cc instanceof SyLabelReadableType);
      Assert.assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    final PAreaI<SySpaceViewportType> box = w0.box();
    Assert.assertEquals(15L, (long) box.minimumX());
    Assert.assertEquals(10L, (long) box.minimumY());
    Assert.assertEquals(640L, (long) box.sizeX());
    Assert.assertEquals(480L, (long) box.sizeY());
  }

  @Test
  public final void testWindowClickNothing()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseDown(
          PVector2I.of(800, 600),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertFalse(c.isPresent());
    }

    {
      final Optional<SyComponentType> c =
        g.onMouseUp(
          PVector2I.of(800, 600),
          SyMouseButton.MOUSE_BUTTON_LEFT);
      Assert.assertFalse(c.isPresent());
    }
  }

  @Test
  public final void testWindowMouseOverTitlebarDragRightButton()
  {
    final SyTheme t = SyThemeMotive.builder().build();
    final SyGUIType g = this.createWithTheme("main", t);
    final SyWindowType w0 = g.windowCreate(640, 480, "Window 0");

    {
      final Optional<SyComponentType> c =
        g.onMouseDown(
          PVector2I.of(320, 10),
          SyMouseButton.MOUSE_BUTTON_RIGHT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(
        cc instanceof SyLabelReadableType);
      Assert.assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    g.onMouseMoved(PVector2I.of(320 + 15, 20));

    {
      final Optional<SyComponentType> c =
        g.onMouseUp(
          PVector2I.of(320 + 15, 20),
          SyMouseButton.MOUSE_BUTTON_RIGHT);
      Assert.assertTrue(c.isPresent());
      final SyComponentType cc = c.get();
      Assert.assertTrue(
        cc instanceof SyLabelReadableType);
      Assert.assertTrue(
        cc.node().parent().get().value() instanceof SyWindowTitleBarType);
    }

    g.onMouseMoved(PVector2I.of(800, 600));

    final PAreaI<SySpaceViewportType> box = w0.box();
    Assert.assertEquals(0L, (long) box.minimumX());
    Assert.assertEquals(0L, (long) box.minimumY());
    Assert.assertEquals(640L, (long) box.sizeX());
    Assert.assertEquals(480L, (long) box.sizeY());
  }
}
