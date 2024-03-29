/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.awt.internal;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceTextAlignedType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.text.SyTextID;
import com.io7m.jsycamore.api.text.SyTextLineMeasuredType;
import com.io7m.jsycamore.api.text.SyTextLineNumber;
import com.io7m.jsycamore.api.text.SyTextLocationType;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;

import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.util.Comparator;
import java.util.Objects;

/**
 * Analyzed text.
 */

public final class SyAWTTextAnalyzed implements SyTextLineMeasuredType
{
  private final int pageWidth;
  private final TextLayout layout;
  private final SyText textAsWrapped;
  private final SyTextID textOriginal;
  private final int textHeight;
  private final int textWidth;

  /**
   * Create a section of analyzed text.
   *
   * @param inPageWidth     The page width
   * @param inTextWidth     The text width
   * @param inTextHeight    The text height
   * @param inLayout        The text layout
   * @param inTextOriginal  The original text that was analyzed
   * @param inTextAsWrapped The text after analysis and wrapping
   */

  public SyAWTTextAnalyzed(
    final int inPageWidth,
    final int inTextWidth,
    final int inTextHeight,
    final TextLayout inLayout,
    final SyTextID inTextOriginal,
    final SyText inTextAsWrapped)
  {
    this.pageWidth =
      inPageWidth;
    this.textWidth =
      inTextWidth;
    this.textHeight =
      inTextHeight;
    this.textOriginal =
      Objects.requireNonNull(inTextOriginal, "inTextOriginal");
    this.textAsWrapped =
      Objects.requireNonNull(inTextAsWrapped, "text");
    this.layout =
      Objects.requireNonNull(inLayout, "inLayout");
  }

  @Override
  public int pageWidth()
  {
    return this.pageWidth;
  }

  @Override
  public int height()
  {
    return this.textHeight;
  }

  @Override
  public int textWidth()
  {
    return this.textWidth;
  }

  @Override
  public SyTextID textOriginal()
  {
    return this.textOriginal;
  }

  @Override
  public SyText textAsWrapped()
  {
    return this.textAsWrapped;
  }

  @Override
  public SyTextLocationType inspectAt(
    final SyTextLineNumber lineNumber,
    final PVector2I<SySpaceTextAlignedType> position)
  {
    Objects.requireNonNull(lineNumber, "lineNumber");
    Objects.requireNonNull(position, "position");

    final var hitInfo =
      this.layout.hitTestChar(position.x(), position.y());
    final var caret =
      this.layout.getCaretShape(hitInfo);
    final var caretBounds =
      caret.getBounds();

    final var caretArea =
      PAreasI.<SySpaceParentRelativeType>create(
        caretBounds.x,
        caretBounds.y,
        caretBounds.width,
        caretBounds.height
      );

    return new SyTextLocation(this, lineNumber, hitInfo, caretArea);
  }

  @Override
  public PVector2I<SySpaceTextAlignedType> transformToTextCoordinates(
    final PVector2I<SySpaceParentRelativeType> position)
  {
    Objects.requireNonNull(position, "position");

    return switch (this.textAsWrapped.direction()) {
      case TEXT_DIRECTION_LEFT_TO_RIGHT -> {
        yield PVector2I.of(position.x(), position.y());
      }

      /*
       * The underlying AWT TextLayout already takes into account RTL text,
       * but it obviously has no way to take into the account that we're
       * manually right-aligning RTL text. It's necessary to subtract the
       * amount by which we shift the text in order to hit the correct
       * character.
       */

      case TEXT_DIRECTION_RIGHT_TO_LEFT -> {
        final var delta = this.pageWidth - this.textWidth;
        yield PVector2I.of(position.x() - delta, position.y());
      }
    };
  }

  private static final class SyTextLocation
    implements SyTextLocationType
  {
    private final SyAWTTextAnalyzed owner;
    private final SyTextLineNumber lineNumber;
    private final TextHitInfo hitInfo;
    private final SyCaret caret;

    private SyTextLocation(
      final SyAWTTextAnalyzed inOwner,
      final SyTextLineNumber inLineNumber,
      final TextHitInfo inHitInfo,
      final PAreaI<SySpaceParentRelativeType> inArea)
    {
      this.owner =
        Objects.requireNonNull(inOwner, "owner");
      this.lineNumber =
        Objects.requireNonNull(inLineNumber, "lineNumber");
      this.hitInfo =
        Objects.requireNonNull(inHitInfo, "hitInfo");
      this.caret =
        new SyCaret(inArea);
    }

    @Override
    public SyTextLineNumber lineNumber()
    {
      return this.lineNumber;
    }

    @Override
    public SyCharacter characterAt()
    {
      final var text =
        this.owner.textAsWrapped;
      final var textString =
        text.value();

      if (textString.isEmpty()) {
        return new SyCharacter(
          true,
          0,
          (char) 0x0,
          0,
          (char) 0x0
        );
      }

      final var charIndex =
        this.hitInfo.getCharIndex();
      final var insIndex =
        this.hitInfo.getInsertionIndex();

      if (insIndex >= textString.length()) {
        return new SyCharacter(
          this.hitInfo.isLeadingEdge(),
          charIndex,
          textString.charAt(charIndex),
          charIndex,
          textString.charAt(charIndex)
        );
      }

      return new SyCharacter(
        this.hitInfo.isLeadingEdge(),
        charIndex,
        textString.charAt(charIndex),
        insIndex,
        textString.charAt(insIndex)
      );
    }

    @Override
    public String toString()
    {
      return "[SyTextLocation 0x%s %s %d]".formatted(
        Integer.toUnsignedString(this.hashCode(), 16),
        this.lineNumber(),
        Integer.valueOf(this.hitInfo.getCharIndex())
      );
    }

    @Override
    public SyCaret caret()
    {
      return this.caret;
    }

    @Override
    public int compareTo(
      final SyTextLocationType other)
    {
      return Comparator.comparing(SyTextLocationType::lineNumber)
        .thenComparingInt(loc -> loc.characterAt().centerIndex())
        .compare(this, other);
    }
  }
}
