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

package com.io7m.jsycamore.core.components;

import com.io7m.jnull.NullCheck;
import com.io7m.jsycamore.core.SyAlignmentHorizontal;
import com.io7m.jsycamore.core.SyAlignmentVertical;
import com.io7m.jsycamore.core.themes.SyThemeLabelType;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The type of readable text labels.
 */

public interface SyLabelReadableType extends SyComponentReadableType
{
  /**
   * @return The current theme for the label iff the component is attached to a
   * window
   */

  Optional<SyThemeLabelType> theme();

  /**
   * @return The label text
   */

  String text();

  /**
   * Retrieve the horizontal alignment of the text. Text will be aligned within
   * the bounds specified by {@link #box()}.
   *
   * @return The alignment
   */

  SyAlignmentHorizontal textAlignmentHorizontal();

  /**
   * Retrieve the vertical alignment of the text. Text will be aligned within
   * the bounds specified by {@link #box()}.
   *
   * @return The alignment
   */

  SyAlignmentVertical textAlignmentVertical();

  @Override
  default <A, B> B matchComponentReadable(
    final A context,
    final BiFunction<A, SyButtonReadableType, B> on_button,
    final BiFunction<A, SyPanelReadableType, B> on_panel,
    final BiFunction<A, SyLabelReadableType, B> on_label,
    final BiFunction<A, SyImageReadableType, B> on_image)
  {
    return NullCheck.notNull(on_label).apply(context, this);
  }
}
