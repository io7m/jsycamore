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

import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyGUI;
import com.io7m.jsycamore.core.SyGUIType;
import com.io7m.jsycamore.core.SyWindowType;
import com.io7m.jsycamore.core.renderer.SyComponentRendererAWT;
import com.io7m.jsycamore.core.renderer.SyWindowRendererAWT;
import com.io7m.jsycamore.core.renderer.SyWindowRendererType;
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
import com.io7m.jsycamore.core.themes.SyThemeMutable;
import com.io7m.jsycamore.core.themes.SyThemeOutline;
import com.io7m.jsycamore.core.themes.SyThemeOutlineType;
import com.io7m.jsycamore.core.themes.SyThemeWindow;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrame;
import com.io7m.jsycamore.core.themes.SyThemeWindowFrameCorner;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitleBar;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarVerticalPlacement;
import com.io7m.jsycamore.core.themes.SyThemeWindowTitlebarWidthBehavior;
import com.io7m.jtensors.VectorI3F;
import com.io7m.junreachable.UnreachableCodeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

public final class Demo
{
  private Demo()
  {
    throw new UnreachableCodeException();
  }

  public static void main(final String[] args)
    throws Exception
  {
    final BufferedImage bi = new BufferedImage(
      320 + 32, 240 + 32, BufferedImage.TYPE_4BYTE_ABGR_PRE);

    final SyThemeMutable theme = SyThemeMutable.create();

    final SyThemeEmboss.Builder theme_titlebar_emboss_active_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_active_b.setSize(1);
    theme_titlebar_emboss_active_b.setColorTop(
      new VectorI3F(0.44f, 0.52f, 0.58f));
    theme_titlebar_emboss_active_b.setColorLeft(
      new VectorI3F(0.44f, 0.52f, 0.58f));
    theme_titlebar_emboss_active_b.setColorRight(
      new VectorI3F(0.24f, 0.32f, 0.38f));
    theme_titlebar_emboss_active_b.setColorBottom(
      new VectorI3F(0.24f, 0.32f, 0.38f));

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setTextFont("Monospaced 10");
    theme_titlebar_b.setHeight(18);
    theme_titlebar_b.setColorActive(
      new VectorI3F(0.34f, 0.42f, 0.48f));
    theme_titlebar_b.setTextColorActive(
      new VectorI3F(1.0f, 1.0f, 1.0f));
    theme_titlebar_b.setEmbossActive(
      Optional.of(theme_titlebar_emboss_active_b.build()));
    theme_titlebar_b.setEmbossInactive(
      Optional.empty());
    theme_titlebar_b.setTextAlignment(
      SyAlignmentHorizontal.ALIGN_CENTER);
    theme_titlebar_b.setVerticalPlacement(
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_INSIDE_FRAME);
    theme_titlebar_b.setHorizontalAlignment(
      SyAlignmentHorizontal.ALIGN_LEFT);
    theme_titlebar_b.setWidthBehavior(
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_INSIDE_FRAME);

    final SyThemeEmboss.Builder theme_frame_emboss_active_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_active_b.setSize(1);
    theme_frame_emboss_active_b.setColorTop(
      new VectorI3F(0.44f, 0.52f, 0.58f));
    theme_frame_emboss_active_b.setColorLeft(
      new VectorI3F(0.44f, 0.52f, 0.58f));
    theme_frame_emboss_active_b.setColorRight(
      new VectorI3F(0.24f, 0.32f, 0.38f));
    theme_frame_emboss_active_b.setColorBottom(
      new VectorI3F(0.24f, 0.32f, 0.38f));

    final SyThemeWindowFrame.Builder theme_frame_b =
      SyThemeWindowFrame.builder();
    theme_frame_b.setBottomHeight(5);
    theme_frame_b.setTopHeight(5);
    theme_frame_b.setLeftWidth(5);
    theme_frame_b.setRightWidth(5);
    theme_frame_b.setColorActive(new VectorI3F(0.34f, 0.42f, 0.48f));

    theme_frame_b.setTopLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);
    theme_frame_b.setTopRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);
    theme_frame_b.setBottomLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);
    theme_frame_b.setBottomRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_L_PIECE);

    theme_frame_b.setEmbossActive(theme_frame_emboss_active_b.build());
    theme_frame_b.setEmbossInactive(Optional.empty());

    final SyThemeOutline.Builder theme_window_outline =
      SyThemeOutline.builder();
    theme_window_outline.setColorActive(new VectorI3F(0.0f, 0.0f, 0.0f));
    theme_window_outline.setColorInactive(new VectorI3F(0.3f, 0.3f, 0.3f));
    final Optional<SyThemeOutlineType> theme_outline =
      Optional.of(theme_window_outline.build());

    theme.setWindowTheme(
      SyThemeWindow.of(
        theme_titlebar_b.build(),
        theme_frame_b.build(),
        theme_outline));

    final SyGUIType ui = SyGUI.createWithTheme("main", theme);
    final SyWindowType win =
      ui.windowCreate(320, 240, "File Manager - sys$starlet_c");

    final SyWindowRendererType<BufferedImage, BufferedImage> r =
      SyWindowRendererAWT.create(
        ui.textMeasurement(),
        SyComponentRendererAWT.create(ui.textMeasurement()));

    r.render(bi, win);
    ImageIO.write(bi, "PNG", new File("/tmp/window.png"));
  }
}
