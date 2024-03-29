/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.jsycamore.api.windows;

import com.io7m.jattribute.core.AttributeReadableType;
import com.io7m.jorchard.core.JOTreeNodeReadableType;
import com.io7m.jsycamore.api.bounded.SyBoundedReadableType;
import com.io7m.jsycamore.api.components.SyComponentReadableType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.text.SyText;
import com.io7m.jsycamore.api.visibility.SyVisibility;

/**
 * The type of readable windows.
 */

public interface SyWindowReadableType
  extends SyBoundedReadableType<SySpaceViewportType>
{
  /**
   * @return The window ID
   */

  SyWindowID id();

  /**
   * @return The root window component
   */

  JOTreeNodeReadableType<SyComponentReadableType> rootNodeReadable();

  /**
   * @return An attribute indicating if the window is maximized
   */

  AttributeReadableType<Boolean> maximized();

  /**
   * @return An attribute indicating if the window is decorated
   */

  AttributeReadableType<Boolean> decorated();

  /**
   * @return An attribute representing the window title
   */

  AttributeReadableType<SyText> title();

  /**
   * An attribute that, when set to a non-zero value, specifies a snapping value
   * for position values. For a non-zero snapping value {@code n}, the resulting
   * position values will be {@code k * n} for some non-negative {@code k}.
   *
   * @return The position snapping value
   */

  AttributeReadableType<Integer> positionSnapping();

  /**
   * An attribute that, when set to a non-zero value, specifies a snapping value
   * for size values. For a non-zero snapping value {@code n}, the resulting
   * size values will be {@code k * n} for some non-negative {@code k}.
   *
   * @return The size snapping value
   */

  AttributeReadableType<Integer> sizeSnapping();

  /**
   * @return A property denoting the window close button behaviour
   */

  AttributeReadableType<SyWindowCloseBehaviour> closeButtonBehaviour();

  /**
   * @return An attribute denoting the visibility of the window's close button
   */

  AttributeReadableType<SyVisibility> closeButtonVisibility();

  /**
   * @return An attribute denoting the visibility of the window's menu button
   */

  AttributeReadableType<SyVisibility> menuButtonVisibility();

  /**
   * @return An attribute denoting the visibility of the window's maximize
   * button
   */

  AttributeReadableType<SyVisibility> maximizeButtonVisibility();

  /**
   * @return The window layer
   */

  SyWindowLayerID layer();

  /**
   * @return The window deletion policy
   */

  SyWindowDeletionPolicy deletionPolicy();
}
