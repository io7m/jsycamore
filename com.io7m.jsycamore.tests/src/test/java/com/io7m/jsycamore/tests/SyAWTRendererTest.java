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


package com.io7m.jsycamore.tests;

import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.text.SyFontDirectoryType;
import com.io7m.jsycamore.awt.internal.SyAWTRenderer;
import com.io7m.jsycamore.awt.internal.SyFontDirectoryAWT;
import com.io7m.jsycamore.theme.primal.SyThemePrimalFactory;
import com.io7m.jsycamore.vanilla.SyScreenFactory;
import com.io7m.jsycamore.vanilla.internal.SyLayoutContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SyAWTRendererTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SyAWTRendererTest.class);

  private Path directory;
  private SyAWTRenderer renderer;
  private BufferedImage imageReceived;
  private Graphics2D graphics;
  private Path imageReceivedFile;
  private SyFontDirectoryType fonts;
  private SyScreenFactory screens;

  @BeforeEach
  public void setup()
    throws Exception
  {
    this.directory =
      SyTestDirectories.createTempDirectory();
    this.imageReceived =
      new BufferedImage(512, 512, TYPE_4BYTE_ABGR_PRE);
    this.imageReceivedFile =
      this.directory.resolve("received.png");

    this.graphics = this.imageReceived.createGraphics();
    this.graphics.setPaint(Color.BLACK);
    this.graphics.fillRect(0, 0, 512, 512);

    this.fonts = SyFontDirectoryAWT.create();
    this.renderer = new SyAWTRenderer(this.fonts);
    this.screens = new SyScreenFactory();
  }

  @AfterEach
  public void tearDown()
    throws Exception
  {
    SyTestDirectories.deleteDirectory(this.directory);
  }

  private void compareImages(
    final String name)
    throws Exception
  {
    final var expectedPath =
      SyTestDirectories.resourceOf(
        SyAWTRendererTest.class,
        this.directory,
        name
      );

    LOG.debug("-- comparing images");
    LOG.debug("expected {}", expectedPath);
    LOG.debug("received {}", this.imageReceivedFile);

    final var imageExpected =
      ImageIO.read(expectedPath.toFile());

    final var grabR = new PixelGrabber(
      this.imageReceived,
      0,
      0,
      this.imageReceived.getWidth(),
      this.imageReceived.getHeight(),
      false
    );

    final var grabE = new PixelGrabber(
      imageExpected,
      0,
      0,
      imageExpected.getWidth(),
      imageExpected.getHeight(),
      false
    );

    assertTrue(grabR.grabPixels(), "Grabbing pixels must succeed");
    assertTrue(grabE.grabPixels(), "Grabbing pixels must succeed");

    final var pixelsR = (int[]) grabR.getPixels();
    final var pixelsE = (int[]) grabE.getPixels();

    try {
      assertArrayEquals(pixelsE, pixelsR, "Images do not match");
    } catch (final AssertionFailedError e) {
      final var fileExp =
        Files.createTempFile("image-expected-", ".png");
      final var fileRec =
        Files.createTempFile("image-received-", ".png");

      Files.copy(this.imageReceivedFile, fileRec, REPLACE_EXISTING);
      ImageIO.write(imageExpected, "PNG", fileExp.toFile());

      LOG.info("expected: {}", fileExp);
      LOG.info("received: {}", fileRec);
      throw e;
    }
  }

  private void saveImage()
    throws Exception
  {
    ImageIO.write(
      this.imageReceived,
      "PNG",
      this.imageReceivedFile.toFile()
    );

    ImageIO.write(
      this.imageReceived,
      "PNG",
      new File("/tmp/out.png")
    );
  }

  @Test
  public void testWindowSimple0()
    throws Exception
  {
    final var theme =
      new SyThemePrimalFactory()
        .create();
    final var layoutContext =
      new SyLayoutContext(this.fonts, theme);

    final var screen =
      this.screens.create(theme, this.fonts, PAreaSizeI.of(512, 512));
    final var window =
      screen.windowCreate(512, 512);

    screen.windowShow(window);

    window.decorated().set(true);
    window.title().set("Window Title");
    window.layout(layoutContext);

    /*
     * Render the image twice: The window will cause a few images to be loaded,
     * and those images won't be immediately available due to the image loader
     * cache. After waiting 100ms, they'll almost certainly be ready the second
     * time around.
     */

    this.renderer.render(this.graphics, screen, window);
    Thread.sleep(500L);
    this.renderer.render(this.graphics, screen, window);

    this.saveImage();
    this.compareImages("testWindow0.png");
  }
}
