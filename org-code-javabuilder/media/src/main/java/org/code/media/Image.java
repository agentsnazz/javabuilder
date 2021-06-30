package org.code.media;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.code.protocol.GlobalProtocol;

public class Image {
  private Pixel[] pixels;
  private int width;
  private int height;

  /**
   * Creates a new image object, using the pixel information from the file uploaded to the asset
   * manager.
   *
   * @param filename the name of the image loaded into the asset manager for the project
   * @throws FileNotFoundException if the file doesn't exist in the asset manager.
   */
  public Image(String filename) throws FileNotFoundException {
    try {
      BufferedImage bufferedImage =
          ImageIO.read(new URL(GlobalProtocol.getInstance().generateAssetUrl(filename)));
      this.width = bufferedImage.getWidth();
      this.height = bufferedImage.getHeight();
      this.pixels = new Pixel[this.width * this.height];

      for (int x = 0; x < this.width; x++) {
        for (int y = 0; y < this.height; y++) {
          int rgbColor = bufferedImage.getRGB(x, y);
          this.pixels[this.getPixelIndex(x, y)] = new Pixel(this, x, y, new Color(rgbColor));
        }
      }
    } catch (IOException e) {
      // TODO: improve error handling
      throw new FileNotFoundException();
    }
  }

  /**
   * Create a new image object, copying the source image provided.
   *
   * @param source the image to duplicate
   */
  public Image(Image source) {
    this.width = source.getWidth();
    this.height = source.getHeight();
    this.pixels = new Pixel[this.width * this.height];
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        Color sourceColor = source.getPixel(x, y).getColor();
        this.pixels[this.getPixelIndex(x, y)] =
            new Pixel(
                this,
                x,
                y,
                new Color(sourceColor.getRed(), sourceColor.getGreen(), sourceColor.getBlue()));
      }
    }
  }

  /**
   * Creates an empty image filled with white pixels.
   *
   * @param width the width of the image to create.
   * @param height the height of the image to create.
   */
  public Image(int width, int height) {
    this.pixels = new Pixel[width * height];
    this.width = width;
    this.height = height;
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        this.pixels[this.getPixelIndex(x, y)] = new Pixel(this, x, y, Color.WHITE);
      }
    }
  }

  /**
   * Get an array with all of the pixels of the image.
   *
   * @return the pixels in the image. This array will have a length equals to the width multiplied
   *     by the height.
   */
  public Pixel[] getPixels() {
    return this.pixels;
  }

  /**
   * Get the color value at the pixel specified.
   *
   * @param x the x position of the pixel
   * @param y the y position of the pixel
   * @return the color of the pixel
   */
  public Pixel getPixel(int x, int y) {
    return this.pixels[this.getPixelIndex(x, y)];
  }

  /**
   * Set the color value at the pixel specified.
   *
   * @param x the x position of the pixel
   * @param y the y position of the pixel
   * @param color the color to set the pixel
   */
  public void setPixel(int x, int y, Color color) {
    this.pixels[this.getPixelIndex(x, y)].setColor(color);
  }

  /**
   * Gets the width of the image in pixels.
   *
   * @return the width of the image in pixels.
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Gets the height of the image in pixels.
   *
   * @return the height of the image in pixels.
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Clears the image, filling it with the color provided.
   *
   * @param color the color with which to fill the image.
   */
  public void clear(Color color) {
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        this.pixels[this.getPixelIndex(x, y)].setColor(color);
      }
    }
  }

  public BufferedImage getBufferedImage() {
    BufferedImage bufferedImage =
        new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        bufferedImage.setRGB(x, y, this.pixels[this.getPixelIndex(x, y)].getColor().getRGB());
      }
    }
    return bufferedImage;
  }

  private int getPixelIndex(int x, int y) {
    return (y * this.width) + x;
  }
}
