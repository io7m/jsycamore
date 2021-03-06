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

package com.io7m.jsycamore.tests.core;

import com.io7m.jregions.core.parameterized.areas.PAreaI;
import com.io7m.jregions.core.parameterized.areas.PAreasI;
import com.io7m.jsycamore.api.SyGUI;
import com.io7m.jsycamore.api.SyGUIType;
import com.io7m.jsycamore.api.SyMouseButton;
import com.io7m.jsycamore.api.SyParentResizeBehavior;
import com.io7m.jsycamore.api.components.SyActive;
import com.io7m.jsycamore.api.components.SyButtonCheckbox;
import com.io7m.jsycamore.api.components.SyButtonCheckboxType;
import com.io7m.jsycamore.api.components.SyButtonRepeating;
import com.io7m.jsycamore.api.components.SyButtonType;
import com.io7m.jsycamore.api.components.SyLabel;
import com.io7m.jsycamore.api.components.SyLabelType;
import com.io7m.jsycamore.api.components.SyMeter;
import com.io7m.jsycamore.api.components.SyMeterType;
import com.io7m.jsycamore.api.components.SyPanel;
import com.io7m.jsycamore.api.components.SyPanelType;
import com.io7m.jsycamore.api.images.SyImageCacheLoaderType;
import com.io7m.jsycamore.api.images.SyImageCacheResolverType;
import com.io7m.jsycamore.api.images.SyImageCacheType;
import com.io7m.jsycamore.api.images.SyImageFormat;
import com.io7m.jsycamore.api.images.SyImageScaleInterpolation;
import com.io7m.jsycamore.api.images.SyImageSpecification;
import com.io7m.jsycamore.api.renderer.SyComponentRendererType;
import com.io7m.jsycamore.api.renderer.SyWindowRendererType;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.spaces.SySpaceViewportType;
import com.io7m.jsycamore.api.themes.SyOrientation;
import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeProviderType;
import com.io7m.jsycamore.api.windows.SyWindowContentPaneType;
import com.io7m.jsycamore.api.windows.SyWindowType;
import com.io7m.jsycamore.awt.SyAWTComponentRenderer;
import com.io7m.jsycamore.awt.SyAWTComponentRendererContextType;
import com.io7m.jsycamore.awt.SyAWTTextMeasurement;
import com.io7m.jsycamore.awt.SyAWTWindowRenderer;
import com.io7m.jsycamore.caffeine.SyBufferedImageCacheCaffeine;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.junreachable.UnreachableCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class WindowDemo
{
  private WindowDemo()
  {
    throw new UnreachableCodeException();
  }

  public static void main(final String[] args)
  {
    SwingUtilities.invokeLater(() -> {
      try {
        final JFrame frame = new JFrame("WindowDemo");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(new Canvas());
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private static final class Canvas extends JPanel
  {
    private static final Logger LOG;

    static {
      LOG = LoggerFactory.getLogger(Canvas.class);
    }

    private final SyAWTTextMeasurement measurement;
    private final SyGUIType gui;
    private final SyWindowType window0;
    private final SyWindowType window1;
    private final SyImageCacheType<BufferedImage> image_cache;
    private final SyComponentRendererType<SyAWTComponentRendererContextType, BufferedImage> c_renderer;
    private final SyWindowRendererType<BufferedImage, BufferedImage> w_renderer;
    private final List<SyTheme> themes;
    private final Random random;

    Canvas()
      throws Exception
    {
      this.setFocusable(true);

      this.random = new Random(23L);

      this.themes = new ArrayList<>();
      {
        final ServiceLoader<SyThemeProviderType> loader =
          ServiceLoader.load(SyThemeProviderType.class);
        final Iterator<SyThemeProviderType> iter = loader.iterator();
        while (iter.hasNext()) {
          final SyThemeProviderType provider = iter.next();
          LOG.debug("loaded theme: {}", provider.name());
          this.themes.add(provider.theme());
        }
      }

      final SyImageCacheResolverType resolver = specification -> {
        final URI uri = specification.uri();
        LOG.debug("loading: {}", uri);

        final URL url = uri.toURL();
        final InputStream stream = url.openStream();
        if (stream == null) {
          throw new FileNotFoundException(url.toString());
        }
        return stream;
      };

      final SyImageCacheLoaderType<BufferedImage> loader = (specification, stream) -> {
        final BufferedImage loaded = ImageIO.read(stream);
        if (loaded == null) {
          throw new IOException(
            "Could not parse output_image " + specification.uri());
        }
        return loaded;
      };

      final ExecutorService io_executor =
        Executors.newFixedThreadPool(4);
      final ScheduledExecutorService r_executor =
        Executors.newSingleThreadScheduledExecutor();

      final BufferedImage image_default =
        new BufferedImage(4, 4, BufferedImage.TYPE_4BYTE_ABGR);
      final BufferedImage image_error =
        image_default;

      this.image_cache = SyBufferedImageCacheCaffeine.create(
        resolver, loader, io_executor, image_default, image_error, 4_000_000L);

      this.measurement = SyAWTTextMeasurement.create();
      this.gui =
        SyGUI.createWithTheme(this.measurement, "Main", this.themes.get(0));

      this.window0 = this.createWindow("Window 0");
      this.window0.setBox(PAreasI.create(16, 16, 320, 240));
      this.window1 = this.createWindow("Window 1");
      this.window1.setBox(PAreasI.create(16, 16 + 240 + 16, 320, 240));

      this.c_renderer = SyAWTComponentRenderer.create(
        this.image_cache, this.measurement, this.measurement);
      this.w_renderer = SyAWTWindowRenderer.create(this.c_renderer);

      final MouseAdapter mouse_adapter = new MouseAdapter()
      {
        @Override
        public void mousePressed(final MouseEvent e)
        {
          Canvas.this.gui.onMouseDown(
            PVector2I.of(e.getX(), e.getY()),
            SyMouseButton.ofIndex(e.getButton() - 1));
          Canvas.this.repaint();
        }

        @Override
        public void mouseDragged(final MouseEvent e)
        {
          Canvas.this.gui.onMouseMoved(PVector2I.of(e.getX(), e.getY()));
          Canvas.this.repaint();
        }

        @Override
        public void mouseReleased(final MouseEvent e)
        {
          Canvas.this.gui.onMouseUp(
            PVector2I.of(e.getX(), e.getY()),
            SyMouseButton.ofIndex(e.getButton() - 1));
          Canvas.this.repaint();
        }

        @Override
        public void mouseMoved(final MouseEvent e)
        {
          Canvas.this.gui.onMouseMoved(PVector2I.of(e.getX(), e.getY()));
          Canvas.this.repaint();
        }
      };

      final KeyAdapter key_adapter = new KeyAdapter()
      {
        @Override
        public void keyPressed(final KeyEvent e)
        {
          System.out.println(e);
        }

        @Override
        public void keyReleased(final KeyEvent e)
        {
          System.out.println(e);
        }
      };

      this.addMouseMotionListener(mouse_adapter);
      this.addMouseListener(mouse_adapter);
      this.addKeyListener(key_adapter);

      r_executor.scheduleAtFixedRate(
        () -> SwingUtilities.invokeLater(() -> {

          LOG.debug(
            "cache size: {}/{}",
            Long.valueOf(this.image_cache.size()),
            Long.valueOf(this.image_cache.maximumSize()));

          if (!this.window0.isOpen()) {
            this.gui.windowOpen(this.window0);
          }
          if (!this.window1.isOpen()) {
            this.gui.windowOpen(this.window1);
          }
          this.repaint();
        }), 3L, 3L, TimeUnit.SECONDS);

      r_executor.scheduleAtFixedRate(
        () -> SwingUtilities.invokeLater(() -> {
          Collections.shuffle(this.themes, this.random);
          this.gui.setTheme(this.themes.get(0));
          this.repaint();
        }), 3L, 3L, TimeUnit.SECONDS);
    }

    private SyWindowType createWindow(
      final String title)
      throws URISyntaxException
    {
      final SyWindowType window = this.gui.windowCreate(320, 240, title);
      window.titleBar().setIcon(Optional.of(SyImageSpecification.of(
        WindowDemo.class.getResource("/com/io7m/jsycamore/tests/awt/paper.png").toURI(),
        16,
        16,
        SyImageFormat.IMAGE_FORMAT_RGBA_8888,
        Vector4D.of(1.0, 1.0, 1.0, 1.0),
        SyImageScaleInterpolation.SCALE_INTERPOLATION_NEAREST)));

      final SyWindowContentPaneType content = window.contentPane();

      {
        final SyPanelType panel = SyPanel.create();
        panel.setResizeBehaviorWidth(SyParentResizeBehavior.BEHAVIOR_RESIZE);
        panel.setResizeBehaviorHeight(SyParentResizeBehavior.BEHAVIOR_RESIZE);

        final PAreaI<SySpaceParentRelativeType> content_box = content.box();
        panel.setBox(PAreasI.create(
          0,
          0,
          content_box.sizeX(),
          content_box.sizeY()));
        content.node().childAdd(panel.node());

        {
          final SyButtonType button = SyButtonRepeating.create();
          button.setBox(PAreasI.create(8, 8, 64, 32));
          panel.node().childAdd(button.node());

          final SyLabelType label = SyLabel.create();
          label.setText("Hello");
          label.setBox(PAreasI.create(0, 0, 64, 32));
          button.node().childAdd(label.node());
        }

        {
          final SyButtonType button = SyButtonRepeating.create();
          button.setBox(PAreasI.create(8 + 64 + 8, 8, 64, 32));
          button.setActive(SyActive.INACTIVE);
          panel.node().childAdd(button.node());

          final SyLabelType label = SyLabel.create();
          label.setText("Hello");
          label.setBox(PAreasI.create(0, 0, 64, 32));
          button.node().childAdd(label.node());
        }

        {
          int y_index = 0;
          final int y_start = 8;
          final int y_end = y_start + (4 * (16 + 2));
          for (int y = y_start; y < y_end; y += 16 + 2) {
            final int x_start = 8 + 64 + 8 + 64 + 8;
            final int x_end = x_start + (4 * (16 + 2));
            int x_index = 0;
            for (int x = x_start; x < x_end; x += 16 + 2) {
              final SyButtonCheckboxType button = SyButtonCheckbox.create();
              button.setBox(PAreasI.create(x, y, 16, 16));
              panel.node().childAdd(button.node());

              if (x_index % 2 == 0 && y_index % 2 == 0) {
                button.setActive(SyActive.INACTIVE);
              }
              if (x_index % 3 == 0 && y_index % 3 == 0) {
                button.setChecked(true);
              }

              ++x_index;
            }
            ++y_index;
          }
        }

        {
          final SyMeterType meter = SyMeter.create();
          meter.setBox(PAreasI.create(224 + 8, 8, 16, 128));
          meter.setOrientation(SyOrientation.ORIENTATION_VERTICAL);
          meter.setActive(SyActive.ACTIVE);
          meter.setValue(0.75);
          panel.node().childAdd(meter.node());
        }

        {
          final SyMeterType meter = SyMeter.create();
          meter.setBox(PAreasI.create(224 + 8 + 16 + 8, 8, 16, 128));
          meter.setOrientation(SyOrientation.ORIENTATION_VERTICAL);
          meter.setActive(SyActive.INACTIVE);
          meter.setValue(0.4);
          panel.node().childAdd(meter.node());
        }

        {
          final SyMeterType meter = SyMeter.create();
          meter.setBox(PAreasI.create(8, 80 + 16, 128, 16));
          meter.setActive(SyActive.ACTIVE);
          meter.setValue(0.75);
          panel.node().childAdd(meter.node());
        }

        {
          final SyMeterType meter = SyMeter.create();
          meter.setBox(PAreasI.create(8, 80 + 16 + 8 + 16, 128, 16));
          meter.setActive(SyActive.INACTIVE);
          meter.setValue(0.4);
          panel.node().childAdd(meter.node());
        }
      }

      return window;
    }

    @Override
    public void paint(final Graphics g)
    {
      super.paint(g);

      final List<SyWindowType> order = this.gui.windowsOpenOrdered();
      final ListIterator<SyWindowType> iter = order.listIterator(order.size());

      while (iter.hasPrevious()) {
        final SyWindowType w = iter.previous();
        this.renderWindow(g, w);
      }
    }

    private void renderWindow(
      final Graphics g,
      final SyWindowType w)
    {
      final PAreaI<SySpaceViewportType> box = w.box();
      final BufferedImage image = new BufferedImage(
        box.sizeX(), box.sizeY(), BufferedImage.TYPE_4BYTE_ABGR);
      this.w_renderer.render(image, w);
      g.drawImage(image, box.minimumX(), box.minimumY(), null);
    }
  }
}
