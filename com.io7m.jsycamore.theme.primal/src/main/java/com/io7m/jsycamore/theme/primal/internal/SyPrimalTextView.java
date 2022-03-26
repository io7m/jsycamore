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

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.components.SyTextViewReadableType;
import com.io7m.jsycamore.api.rendering.SyRenderNodeNoop;
import com.io7m.jsycamore.api.rendering.SyRenderNodeText;
import com.io7m.jsycamore.api.rendering.SyRenderNodeType;
import com.io7m.jsycamore.api.themes.SyThemeContextType;

import java.util.Objects;

import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.TEXT_COLOR;

/**
 * A theme component for text views.
 */

public final class SyPrimalTextView extends SyPrimalAbstract
{
  /**
   * A theme component for text views.
   *
   * @param inTheme The theme
   */

  public SyPrimalTextView(
    final SyThemePrimal inTheme)
  {
    super(inTheme);
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

    if (component instanceof SyTextViewReadableType textView) {
      final var theme = this.theme();
      return new SyRenderNodeText(
        theme.parameterForFillRGBA(TEXT_COLOR).orElseThrow(),
        PAreaSizeI.of(area.sizeX(), area.sizeY()),
        this.font(context, component),
        textView.text().get()
      );
    }
    return new SyRenderNodeNoop();
  }
}
