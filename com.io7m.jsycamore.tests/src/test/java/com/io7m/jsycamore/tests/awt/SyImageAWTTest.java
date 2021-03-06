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

package com.io7m.jsycamore.tests.awt;

import com.io7m.jsycamore.api.images.SyImageFormat;
import com.io7m.jsycamore.api.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.awt.SyAWTImage;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public final class SyImageAWTTest
{
  private static BufferedImage load(final String s)
    throws IOException
  {
    try (final InputStream is = SyImageAWTTest.class.getResourceAsStream(s)) {
      return ImageIO.read(is);
    }
  }

  @Test
  public void testFilterIdentity()
    throws IOException
  {
    final BufferedImage image = load("circle-x-8x.png");

    Assert.assertEquals(64L, (long) image.getWidth());
    Assert.assertEquals(64L, (long) image.getHeight());

    final SyImageSpecification spec = SyImageSpecification.of(
      URI.create("anything"),
      64,
      64,
      SyImageFormat.IMAGE_FORMAT_RGBA_8888,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    final BufferedImage filtered = SyAWTImage.filter(spec, image);
    Assert.assertSame(image, filtered);
  }

  @Test
  public void testFilterHalfSize()
    throws IOException
  {
    final BufferedImage image = load("circle-x-8x.png");

    Assert.assertEquals(64L, (long) image.getWidth());
    Assert.assertEquals(64L, (long) image.getHeight());

    for (final SyImageScaleInterpolation v : SyImageScaleInterpolation.values()) {
      final SyImageSpecification spec = SyImageSpecification.of(
        URI.create("anything"),
        32,
        32,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        v);

      final BufferedImage filtered = SyAWTImage.filter(spec, image);
      Assert.assertNotSame(image, filtered);

      Assert.assertEquals(32L, (long) filtered.getWidth());
      Assert.assertEquals(32L, (long) filtered.getHeight());

      final BufferedImage filtered_again = SyAWTImage.filter(spec, filtered);
      Assert.assertSame(filtered_again, filtered);
    }
  }

  @Test
  public void testFilterRGB565()
    throws IOException
  {
    final BufferedImage image = load("circle-x-8x.png");

    Assert.assertEquals(64L, (long) image.getWidth());
    Assert.assertEquals(64L, (long) image.getHeight());

    final SyImageSpecification spec = SyImageSpecification.of(
      URI.create("anything"),
      64,
      64,
      SyImageFormat.IMAGE_FORMAT_RGB_565,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    final BufferedImage filtered = SyAWTImage.filter(spec, image);
    Assert.assertNotSame(image, filtered);

    Assert.assertEquals(64L, (long) filtered.getWidth());
    Assert.assertEquals(64L, (long) filtered.getHeight());
    Assert.assertEquals(
      (long) BufferedImage.TYPE_USHORT_565_RGB,
      (long) filtered.getType());

    final BufferedImage filtered_again = SyAWTImage.filter(spec, filtered);
    Assert.assertSame(filtered_again, filtered);
  }

  @Test
  public void testFilterRGB888()
    throws IOException
  {
    final BufferedImage image = load("circle-x-8x.png");

    Assert.assertEquals(64L, (long) image.getWidth());
    Assert.assertEquals(64L, (long) image.getHeight());

    final SyImageSpecification spec = SyImageSpecification.of(
      URI.create("anything"),
      64,
      64,
      SyImageFormat.IMAGE_FORMAT_RGB_888,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    final BufferedImage filtered = SyAWTImage.filter(spec, image);
    Assert.assertNotSame(image, filtered);

    Assert.assertEquals(64L, (long) filtered.getWidth());
    Assert.assertEquals(64L, (long) filtered.getHeight());
    Assert.assertEquals(
      (long) BufferedImage.TYPE_3BYTE_BGR,
      (long) filtered.getType());

    final BufferedImage filtered_again = SyAWTImage.filter(spec, filtered);
    Assert.assertSame(filtered_again, filtered);
  }

  @Test
  public void testFilterGrey8()
    throws IOException
  {
    final BufferedImage image = load("circle-x-8x.png");

    Assert.assertEquals(64L, (long) image.getWidth());
    Assert.assertEquals(64L, (long) image.getHeight());

    final SyImageSpecification spec = SyImageSpecification.of(
      URI.create("anything"),
      64,
      64,
      SyImageFormat.IMAGE_FORMAT_GREY_8,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    final BufferedImage filtered = SyAWTImage.filter(spec, image);
    Assert.assertNotSame(image, filtered);

    Assert.assertEquals(64L, (long) filtered.getWidth());
    Assert.assertEquals(64L, (long) filtered.getHeight());
    Assert.assertEquals(
      (long) BufferedImage.TYPE_BYTE_GRAY,
      (long) filtered.getType());

    final BufferedImage filtered_again = SyAWTImage.filter(spec, filtered);
    Assert.assertSame(filtered_again, filtered);
  }

  @Test
  public void testFilterRGBA4444()
    throws IOException
  {
    final BufferedImage image = load("circle-x-8x.png");

    Assert.assertEquals(64L, (long) image.getWidth());
    Assert.assertEquals(64L, (long) image.getHeight());

    final SyImageSpecification spec = SyImageSpecification.of(
      URI.create("anything"),
      64,
      64,
      SyImageFormat.IMAGE_FORMAT_RGBA_4444,
      Vector4D.of(1.0, 1.0, 1.0, 1.0),
      SyImageScaleInterpolation.SCALE_INTERPOLATION_BILINEAR);

    final BufferedImage filtered = SyAWTImage.filter(spec, image);
    Assert.assertNotSame(image, filtered);

    Assert.assertEquals(64L, (long) filtered.getWidth());
    Assert.assertEquals(64L, (long) filtered.getHeight());
    Assert.assertEquals(
      (long) BufferedImage.TYPE_CUSTOM,
      (long) filtered.getType());
  }
}
