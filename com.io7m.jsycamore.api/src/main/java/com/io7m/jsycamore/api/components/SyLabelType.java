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

package com.io7m.jsycamore.api.components;

import com.io7m.jsycamore.api.themes.SyAlignmentHorizontal;
import com.io7m.jsycamore.api.themes.SyAlignmentVertical;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * The type of labels.
 */

public interface SyLabelType extends SyComponentType, SyLabelReadableType
{
  /**
   * Set the label text.
   *
   * @param text The new text
   */

  void setText(String text);

  /**
   * Set the horizontal alighnment of the text.
   *
   * @param h The alignment
   *
   * @see #textAlignmentHorizontal()
   */

  void setTextAlignmentHorizontal(SyAlignmentHorizontal h);

  /**
   * Set the vertical alighnment of the text.
   *
   * @param v The alignment
   *
   * @see #textAlignmentVertical()
   */

  void setTextAlignmentVertical(SyAlignmentVertical v);

  @Override
  default <A, B> B matchComponent(
    final A context,
    final BiFunction<A, SyButtonType, B> on_button,
    final BiFunction<A, SyPanelType, B> on_panel,
    final BiFunction<A, SyLabelType, B> on_label,
    final BiFunction<A, SyImageType, B> on_image,
    final BiFunction<A, SyMeterType, B> on_meter)
  {
    return Objects.requireNonNull(on_label, "Receiver").apply(context, this);
  }
}
