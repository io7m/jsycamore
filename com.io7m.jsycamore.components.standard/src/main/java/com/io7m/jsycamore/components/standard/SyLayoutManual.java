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


package com.io7m.jsycamore.components.standard;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.screens.SyScreenType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameType;

import java.util.List;

import static java.lang.Math.min;

/**
 * A layout that respects manually configured positions and sizes.
 */

public final class SyLayoutManual extends SyLayoutAbstract
{
  /**
   * A layout that respects manually configured positions and sizes.
   *
   * @param screen              The screen that owns the component
   * @param inThemeClassesExtra The extra theme classes, if any
   */

  public SyLayoutManual(
    final SyScreenType screen,
    final List<SyThemeClassNameType> inThemeClassesExtra)
  {
    super(screen, inThemeClassesExtra);
  }

  /**
   * A layout that respects manually configured positions and sizes.
   *
   * @param screen The screen that owns the component
   */

  @ConvenienceConstructor
  public SyLayoutManual(final SyScreenType screen)
  {
    this(screen, List.of());
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var sizeLimit =
      this.sizeUpperLimit().get();
    final var containerSizeX =
      min(constraints.sizeMaximumX(), sizeLimit.sizeX());
    final var containerSizeY =
      min(constraints.sizeMaximumY(), sizeLimit.sizeY());

    final var childNodes = this.node().children();
    for (final var childNode : childNodes) {
      final var child = childNode.value();
      final var existingSize = child.size().get();
      final var childConstraints =
        new SyConstraints(
          existingSize.sizeX(),
          existingSize.sizeY(),
          existingSize.sizeX(),
          existingSize.sizeY()
        );
      child.layout(layoutContext, childConstraints);
    }

    final PAreaSizeI<SySpaceParentRelativeType> newSize =
      PAreaSizeI.of(containerSizeX, containerSizeY);
    this.setSize(newSize);
    return newSize;
  }
}
