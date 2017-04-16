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

package com.io7m.jsycamore.core.themes;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SySpaceType;
import com.io7m.jsycamore.core.boxes.SyBoxType;
import com.io7m.jsycamore.core.boxes.SyBoxes;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Optional;

/**
 * Functions for dealing with outlines.
 */

public final class SyThemeOutlines
{
  private SyThemeOutlines()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Scale the given box appropriately for the given outline. Specifically, the
   * edges of the box will be inset by {@code 1} pixel for each side that has an
   * outline enabled.
   *
   * @param box     The box
   * @param outline The outline
   * @param <S>     The coordinate space
   *
   * @return A scaled box
   */

  public static <S extends SySpaceType> SyBoxType<S> scaleForOutline(
    final SyBoxType<S> box,
    final SyThemeOutlineType outline)
  {
    NullCheck.notNull(box, "Box");
    NullCheck.notNull(outline, "Outline");

    return SyBoxes.hollowOut(
      box,
      outline.left() ? 1 : 0,
      outline.right() ? 1 : 0,
      outline.top() ? 1 : 0,
      outline.bottom() ? 1 : 0);
  }

  /**
   * Call {@link #scaleForOutline(SyBoxType, SyThemeOutlineType)} if the outline
   * is present, or return {@code box} otherwise.
   *
   * @param box         The box
   * @param outline_opt The optional outline
   * @param <S>         The coordinate space
   *
   * @return A scaled box
   */

  public static <S extends SySpaceType> SyBoxType<S> scaleForOutlineOptional(
    final SyBoxType<S> box,
    final Optional<SyThemeOutlineType> outline_opt)
  {
    NullCheck.notNull(box, "Box");
    NullCheck.notNull(outline_opt, "Outline");

    if (outline_opt.isPresent()) {
      return SyThemeOutlines.scaleForOutline(box, outline_opt.get());
    }
    return box;
  }
}
