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

package com.io7m.jsycamore.tests.core.components;

import com.io7m.jsycamore.api.SyGUI;
import com.io7m.jsycamore.api.SyGUIType;
import com.io7m.jsycamore.api.components.SyImage;
import com.io7m.jsycamore.api.components.SyImageType;
import com.io7m.jsycamore.api.images.SyImageFormat;
import com.io7m.jsycamore.api.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.awt.SyAWTTextMeasurement;
import com.io7m.jsycamore.themes.motive.SyThemeMotive;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;

import java.net.URI;

public final class SyImageTest extends SyImageContract
{
  @Override
  protected SyGUIType gui()
  {
    return SyGUI.createWithTheme(
      SyAWTTextMeasurement.create(),
      "GUI",
      SyThemeMotive.builder().build());
  }

  @Override
  protected SyImageType create()
  {
    final SyImageSpecification spec = SyImageSpecification.of(
      URI.create("default"),
      32,
      32,
      SyImageFormat.IMAGE_FORMAT_RGB_565,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);
    return SyImage.create(spec);
  }
}
