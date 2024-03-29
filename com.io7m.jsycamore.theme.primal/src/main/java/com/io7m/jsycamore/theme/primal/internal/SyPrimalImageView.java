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

package com.io7m.jsycamore.theme.primal.internal;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyImageViewType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeImage;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.spaces.SySpaceComponentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * A theme component for image views.
 */

public final class SyPrimalImageView extends SyPrimalAbstract
{
  /**
   * A theme component for image views.
   *
   * @param inTheme The theme
   */

  public SyPrimalImageView(
    final SyThemePrimal inTheme)
  {
    super(inTheme);
  }

  private static SyRenderNodeType imageOf(
    final PAreaI<SySpaceParentRelativeType> area,
    final URI uri)
  {
    final PAreaSizeI<SySpaceComponentRelativeType> size =
      PAreasI.size(PAreasI.cast(area));

    if (Objects.equals(uri.getScheme(), "jsycamore")) {
      return switch (uri.getSchemeSpecificPart()) {
        case "icon:window_close" -> {
          yield iconOf(size, "window_close.png");
        }
        case "icon:window_maximize" -> {
          yield iconOf(size, "window_maximize.png");
        }
        case "icon:window_menu" -> {
          yield iconOf(size, "window_menu.png");
        }
        case "icon:scroll_left" -> {
          yield iconOf(size, "scroll_left.png");
        }
        case "icon:scroll_right" -> {
          yield iconOf(size, "scroll_right.png");
        }
        case "icon:scroll_up" -> {
          yield iconOf(size, "scroll_up.png");
        }
        case "icon:scroll_down" -> {
          yield iconOf(size, "scroll_down.png");
        }
        case "icon:scroll_h_thumb" -> {
          yield iconOf(size, "scroll_h_thumb.png");
        }
        case "icon:scroll_v_thumb" -> {
          yield iconOf(size, "scroll_v_thumb.png");
        }
        default -> SyRenderNodeNoop.noop();
      };
    }

    return new SyRenderNodeImage("ImageView", uri, PVectors2I.zero(), size);
  }

  private static SyRenderNodeImage iconOf(
    final PAreaSizeI<SySpaceComponentRelativeType> size,
    final String name)
  {
    return new SyRenderNodeImage(
      "ImageView",
      resource(name),
      PVectors2I.zero(),
      size
    );
  }

  private static URI resource(
    final String file)
  {
    try {
      return SyPrimalImageView.class.getResource(
          "/com/io7m/jsycamore/theme/primal/" + file)
        .toURI();
    } catch (final URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public SyRenderNodeType render(
    final SyThemeContextType context,
    final SyComponentReadableType component)
  {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(component, "component");

    final var area =
      component.boundingArea();

    if (component instanceof final SyImageViewType imageView) {
      return imageView.imageURI()
        .get()
        .map(uri -> imageOf(area, uri))
        .orElse(SyRenderNodeNoop.noop());
    }

    return SyRenderNodeNoop.noop();
  }
}
