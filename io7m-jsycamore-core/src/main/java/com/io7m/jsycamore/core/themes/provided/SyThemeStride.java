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

package com.io7m.jsycamore.core.themes.provided;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.themes.SyTheme;
import com.io7m.jsycamore.core.themes.SyThemeAlignment;
import com.io7m.jsycamore.core.themes.SyThemeEmboss;
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

import java.util.Optional;

/**
 * A 1990s style object-based workstation theme.
 */

public final class SyThemeStride
{
  private SyThemeStride()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Create a theme based on the given input values.
   *
   * @param spec The theme-specific input values
   *
   * @return A new theme
   */

  public static SyTheme.Builder builderFrom(
    final SyThemeStrideSpecificationType spec)
  {
    NullCheck.notNull(spec);

    final SyTheme.Builder theme = SyTheme.builder();

    final VectorI3F title_color_active_lighter =
      VectorI3F.scale(spec.titlebarColorActive(), spec.colorLightFactor());
    final VectorI3F title_color_active_darker =
      VectorI3F.scale(spec.titlebarColorActive(), spec.colorDarkFactor());

    final VectorI3F frame_color_lighter =
      VectorI3F.scale(spec.frameColor(), spec.colorLightFactor());
    final VectorI3F frame_color_darker =
      VectorI3F.scale(spec.frameColor(), spec.colorDarkFactor());

    final VectorI3F title_color_inactive_base =
      spec.titlebarColorInactive();
    final VectorI3F color_inactive_lighter =
      VectorI3F.scale(title_color_inactive_base, spec.colorLightFactor());
    final VectorI3F color_inactive_darker =
      VectorI3F.scale(title_color_inactive_base, spec.colorDarkFactor());

    final VectorI3F text_color_active =
      spec.titlebarTextColorActive();
    final VectorI3F text_color_inactive =
      spec.titlebarTextColorInactive();

    final SyThemeEmboss.Builder theme_titlebar_emboss_active_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_active_b.setSize(1);
    theme_titlebar_emboss_active_b.setColorTop(title_color_active_lighter);
    theme_titlebar_emboss_active_b.setColorLeft(title_color_active_lighter);
    theme_titlebar_emboss_active_b.setColorRight(title_color_active_darker);
    theme_titlebar_emboss_active_b.setColorBottom(title_color_active_darker);

    final SyThemeEmboss.Builder theme_titlebar_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_titlebar_emboss_inactive_b.setSize(1);
    theme_titlebar_emboss_inactive_b.setColorTop(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorLeft(color_inactive_lighter);
    theme_titlebar_emboss_inactive_b.setColorRight(color_inactive_darker);
    theme_titlebar_emboss_inactive_b.setColorBottom(color_inactive_darker);

    final SyThemeWindowTitleBar.Builder theme_titlebar_b =
      SyThemeWindowTitleBar.builder();
    theme_titlebar_b.setTextFont("Sans 11");
    theme_titlebar_b.setHeight(18);
    theme_titlebar_b.setColorActive(spec.titlebarColorActive());
    theme_titlebar_b.setColorInactive(title_color_inactive_base);
    theme_titlebar_b.setTextColorActive(text_color_active);
    theme_titlebar_b.setTextColorInactive(text_color_inactive);
    theme_titlebar_b.setEmbossActive(
      Optional.of(theme_titlebar_emboss_active_b.build()));
    theme_titlebar_b.setEmbossInactive(
      Optional.of(theme_titlebar_emboss_inactive_b.build()));
    theme_titlebar_b.setTextAlignment(
      SyThemeAlignment.ALIGN_CENTER);
    theme_titlebar_b.setVerticalPlacement(
      SyThemeWindowTitlebarVerticalPlacement.PLACEMENT_TOP_OVERLAP_FRAME);
    theme_titlebar_b.setHorizontalAlignment(
      SyThemeAlignment.ALIGN_CENTER);
    theme_titlebar_b.setWidthBehavior(
      SyThemeWindowTitlebarWidthBehavior.WIDTH_RESIZE_TO_WINDOW);

    final SyThemeEmboss.Builder theme_frame_emboss_active_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_active_b.setSize(1);
    theme_frame_emboss_active_b.setColorTop(frame_color_lighter);
    theme_frame_emboss_active_b.setColorLeft(frame_color_lighter);
    theme_frame_emboss_active_b.setColorRight(frame_color_darker);
    theme_frame_emboss_active_b.setColorBottom(frame_color_darker);

    final SyThemeEmboss.Builder theme_frame_emboss_inactive_b =
      SyThemeEmboss.builder();
    theme_frame_emboss_inactive_b.setSize(1);
    theme_frame_emboss_inactive_b.setColorTop(frame_color_lighter);
    theme_frame_emboss_inactive_b.setColorLeft(frame_color_lighter);
    theme_frame_emboss_inactive_b.setColorRight(frame_color_darker);
    theme_frame_emboss_inactive_b.setColorBottom(frame_color_darker);

    final SyThemeWindowFrame.Builder theme_frame_b =
      SyThemeWindowFrame.builder();
    theme_frame_b.setBottomHeight(5);
    theme_frame_b.setTopHeight(0);
    theme_frame_b.setLeftWidth(0);
    theme_frame_b.setRightWidth(0);
    theme_frame_b.setColorActive(spec.frameColor());
    theme_frame_b.setColorInactive(spec.frameColor());

    theme_frame_b.setTopLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setTopRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setBottomLeftStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);
    theme_frame_b.setBottomRightStyle(
      SyThemeWindowFrameCorner.FRAME_CORNER_NONE);

    theme_frame_b.setEmbossActive(theme_frame_emboss_active_b.build());
    theme_frame_b.setEmbossInactive(theme_frame_emboss_inactive_b.build());

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

    return theme;
  }

  /**
   * @return A theme builder based on the default values
   */

  public static SyTheme.Builder builder()
  {
    return SyThemeStride.builderFrom(
      SyThemeStrideSpecification.builder().build());
  }
}
