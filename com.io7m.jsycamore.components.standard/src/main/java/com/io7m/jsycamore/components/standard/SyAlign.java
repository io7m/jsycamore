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

import com.io7m.jattribute.core.AttributeType;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyAlignType;
import com.io7m.jsycamore.api.components.SyAlignmentHorizontal;
import com.io7m.jsycamore.api.components.SyAlignmentVertical;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.events.SyEventType;
import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import static com.io7m.jsycamore.api.components.SyAlignmentHorizontal.ALIGN_HORIZONTAL_LEFT;
import static com.io7m.jsycamore.api.components.SyAlignmentVertical.ALIGN_VERTICAL_CENTER;

/**
 * A container that aligns child components to the given horizontal and vertical
 * alignment.
 */

public final class SyAlign extends SyComponentAbstract implements SyAlignType
{
  private final AttributeType<SyAlignmentHorizontal> alignH;
  private final AttributeType<SyAlignmentVertical> alignV;

  /**
   * A container that aligns child components to the given horizontal and
   * vertical alignment.
   */

  public SyAlign()
  {
    final var attributes = SyComponentAttributes.get();
    this.alignH = attributes.create(ALIGN_HORIZONTAL_LEFT);
    this.alignV = attributes.create(ALIGN_VERTICAL_CENTER);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> layout(
    final SyLayoutContextType layoutContext,
    final SyConstraints constraints)
  {
    final var alignHNow = this.alignH.get();
    final var alignVNow = this.alignV.get();

    final var containerArea =
      PAreasI.create(
        0,
        0,
        constraints.sizeMaximumX(),
        constraints.sizeMaximumY()
      );

    final var childNodes = this.node().children();
    for (final var childNode : childNodes) {
      final var component =
        childNode.value();
      final var initial =
        component.layout(layoutContext, constraints);

      var alignedArea =
        PAreasI.create(
          0,
          0,
          initial.sizeX(),
          initial.sizeY()
        );

      alignedArea =
        switch (alignHNow) {
          case ALIGN_HORIZONTAL_LEFT -> {
            yield PAreasI.alignOnXMinX(containerArea, alignedArea);
          }
          case ALIGN_HORIZONTAL_RIGHT -> {
            yield PAreasI.alignOnXMaxX(containerArea, alignedArea);
          }
          case ALIGN_HORIZONTAL_CENTER -> {
            yield PAreasI.alignOnXCenter(containerArea, alignedArea);
          }
        };

      alignedArea =
        switch (alignVNow) {
          case ALIGN_VERTICAL_TOP -> {
            yield PAreasI.alignOnYMinY(containerArea, alignedArea);
          }
          case ALIGN_VERTICAL_BOTTOM -> {
            yield PAreasI.alignOnYMaxY(containerArea, alignedArea);
          }
          case ALIGN_VERTICAL_CENTER -> {
            yield PAreasI.alignOnYCenter(containerArea, alignedArea);
          }
        };

      final var alignedConstraints =
        new SyConstraints(
          constraints.sizeMinimumX(),
          constraints.sizeMinimumY(),
          alignedArea.sizeX(),
          alignedArea.sizeY()
        );

      component.layout(layoutContext, alignedConstraints);
      component.setPosition(
        PVector2I.of(
          alignedArea.minimumX(),
          alignedArea.minimumY()
        )
      );
    }

    final var newSize =
      PAreaSizeI.<SySpaceParentRelativeType>of(
        constraints.sizeMaximumX(),
        constraints.sizeMaximumY());

    this.setSize(newSize);
    return newSize;
  }

  @Override
  protected boolean onEvent(
    final SyEventType event)
  {
    return false;
  }

  @Override
  public AttributeType<SyAlignmentHorizontal> alignmentHorizontal()
  {
    return this.alignH;
  }

  @Override
  public AttributeType<SyAlignmentVertical> alignmentVertical()
  {
    return this.alignV;
  }
}
