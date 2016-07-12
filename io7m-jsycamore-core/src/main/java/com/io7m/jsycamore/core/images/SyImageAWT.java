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

package com.io7m.jsycamore.core.images;

import com.io7m.junreachable.UnreachableCodeException;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Optional;

/**
 * Functions to process {@link java.awt.image.BufferedImage} values.
 */

public final class SyImageAWT
{
  private static final DirectColorModel COLOR_MODEL_RGBA_4444;

  static {
    COLOR_MODEL_RGBA_4444 = new DirectColorModel(
      ColorSpace.getInstance(ColorSpace.CS_sRGB),
      16,
      0b1111_0000_0000_0000,
      0b0000_1111_0000_0000,
      0b0000_0000_1111_0000,
      0b0000_0000_0000_1111,
      false,
      DataBuffer.TYPE_USHORT);
  }

  private SyImageAWT()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Filter the given image, resizing, filtering, and changing format to match
   * the given specification.
   *
   * @param spec  The specification
   * @param image The image
   *
   * @return A filtered image
   */

  public static BufferedImage filter(
    final SyImageSpecificationType spec,
    final BufferedImage image)
  {
    if (SyImageAWT.matchesExpected(spec, image)) {
      return image;
    }
    return SyImageAWT.applyRescale(spec, image);
  }

  private static BufferedImage applyRescale(
    final SyImageSpecificationType spec,
    final BufferedImage image)
  {
    final BufferedImage output =
      SyImageAWT.createCompatible(spec);

    final Graphics2D graphics = output.createGraphics();
    try {
      switch (spec.scaleInterpolation()) {
        case SCALE_INTERPOLATION_BILINEAR: {
          graphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
          break;
        }
        case SCALE_INTERPOLATION_BICUBIC: {
          graphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
          break;
        }
        case SCALE_INTERPOLATION_NEAREST: {
          graphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
          break;
        }
      }

      graphics.drawImage(
        image, 0, 0, output.getWidth(), output.getHeight(), null);
      return output;
    } finally {
      graphics.dispose();
    }
  }

  private static BufferedImage createCompatible(final SyImageSpecificationType spec)
  {
    switch (spec.format()) {
      case IMAGE_FORMAT_GREY_8: {
        return new BufferedImage(
          spec.width(), spec.height(), BufferedImage.TYPE_BYTE_GRAY);
      }
      case IMAGE_FORMAT_RGB_888: {
        return new BufferedImage(
          spec.width(), spec.height(), BufferedImage.TYPE_3BYTE_BGR);
      }
      case IMAGE_FORMAT_RGBA_8888: {
        return new BufferedImage(
          spec.width(), spec.height(), BufferedImage.TYPE_4BYTE_ABGR);
      }
      case IMAGE_FORMAT_RGB_565: {
        return new BufferedImage(
          spec.width(), spec.height(), BufferedImage.TYPE_USHORT_565_RGB);
      }
      case IMAGE_FORMAT_RGBA_4444: {
        final WritableRaster raster =
          SyImageAWT.COLOR_MODEL_RGBA_4444.createCompatibleWritableRaster(
            spec.width(), spec.height());

        @SuppressWarnings("UseOfObsoleteCollectionType")
        final Hashtable<Object, Object> props = new Hashtable<>();
        return new BufferedImage(
          SyImageAWT.COLOR_MODEL_RGBA_4444,
          raster,
          false,
          props);
      }
    }

    throw new UnreachableCodeException();
  }

  private static boolean matchesExpected(
    final SyImageSpecificationType spec,
    final BufferedImage image)
  {
    final boolean size_ok =
      spec.width() == image.getWidth()
        && spec.height() == image.getHeight();

    if (!size_ok) {
      return false;
    }

    final Optional<SyImageFormat> format_opt = SyImageAWT.formatFor(image);
    return format_opt.isPresent() && format_opt.get() == spec.format();
  }

  private static Optional<SyImageFormat> formatFor(final BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_INT_BGR:
      case BufferedImage.TYPE_INT_RGB: {
        return Optional.of(SyImageFormat.IMAGE_FORMAT_RGB_888);
      }
      case BufferedImage.TYPE_4BYTE_ABGR:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_INT_ARGB_PRE: {
        return Optional.of(SyImageFormat.IMAGE_FORMAT_RGBA_8888);
      }
      case BufferedImage.TYPE_BYTE_GRAY: {
        return Optional.of(SyImageFormat.IMAGE_FORMAT_GREY_8);
      }
      case BufferedImage.TYPE_USHORT_565_RGB: {
        return Optional.of(SyImageFormat.IMAGE_FORMAT_RGB_565);
      }
    }
    return Optional.empty();
  }

}
