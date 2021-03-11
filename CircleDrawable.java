package ultra.deep.in.science.is.simple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author Richi on 9/22/2020 AD.
 */
public class CircleDrawable extends TextureRegionDrawable {
    //==============================================================
    // constructors
    //==============================================================
    public CircleDrawable() {
    }
    public CircleDrawable(Pixmap pixmap) {
        setPixmap(pixmap);
    }
    public void setPixmap(Pixmap pixmap) {
        makeCircle(pixmap);
        super.setRegion(new TextureRegion(new Texture(pixmap)));
    }
    @Override public void setRegion(TextureRegion region) {
        if (!region.getTexture().getTextureData().isPrepared())
        {
            region.getTexture().getTextureData().prepare();
        }
        Pixmap origPixmap = region.getTexture().getTextureData().consumePixmap();
        makeCircle(origPixmap);
        super.setRegion(region);
    }
    //==============================================================
    // Overrides
    //==============================================================
    //==============================================================
    // Privates
    //==============================================================
    private void makeCircle(Pixmap pixmap) {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        int min = Math.min(width, height);
        int max = Math.max(width, height);
        boolean is_width_smaller_than_height = width == min;
        double radius = min / 2.0;
        int y_offsetStart = 0;
        int y_offsetEnd = 0;
        int x_offsetStart = 0;
        int x_offsetEnd = 0;
        if (is_width_smaller_than_height)
        {
            y_offsetStart = (int) ((max - min) / 2f);
            y_offsetEnd = (int) (y_offsetStart + min);
            x_offsetEnd = (int) min;
        }
        else
        {
            x_offsetStart = (int) ((max - min) / 2f);
            x_offsetEnd = (int) (x_offsetStart + min);
            y_offsetEnd = (int) min;
        }
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.CLEAR);
        for (int y = 0; y < pixmap.getHeight(); y++)
        {
            for (int x = 0; x < pixmap.getWidth(); x++)
            {
                if (y > y_offsetStart && y < y_offsetEnd)
                {
                    if( x >= x_offsetStart && x < x_offsetEnd)
                    {
                        double dist_x = (radius - x + x_offsetStart);
                        double dist_y = radius - y + y_offsetStart;
                        double dist = Math.sqrt((dist_x * dist_x) + (dist_y * dist_y));
                        if (dist < radius)
                        {
                            continue;
                        }
                    }
                }
                pixmap.drawPixel(x, y);
            }
        }
        pixmap.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
    }
    //==============================================================
    // Nice code for circle PIXMAP
    //==============================================================
    public static Pixmap createCirclePixmap(Pixmap pixmap) {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        int min = Math.min(width, height);
        int max = Math.max(width, height);
        boolean is_width_smaller_than_height = width == min;
        Pixmap round = new Pixmap(min, min, Pixmap.Format.RGBA8888);
        double radius = (min / 2.0f);
        int y_offsetStart = 0;
        int y_offsetEnd = 0;
        int x_offsetStart = 0;
        int x_offsetEnd = 0;
        if (is_width_smaller_than_height)
        {
            y_offsetStart = (int) ((max - min) / 2f);
            y_offsetEnd = (int) (y_offsetStart + min);
            x_offsetEnd = (int) min;
        }
        else
        {
            x_offsetStart = (int) ((max - min) / 2f);
            x_offsetEnd = (int) (x_offsetStart + min);
            y_offsetEnd = (int) min;
        }
        final double ANTI_ALIAS_WIDTH = 3f;
        double antiAliasBandry = radius - ANTI_ALIAS_WIDTH;
        for (int y = y_offsetStart; y < y_offsetEnd; y++)
        {
            for (int x = x_offsetStart; x < x_offsetEnd; x++)
            {
                //check if pixel is outside circle. Set pixel to transparant;
                double dist_x = (radius - x + x_offsetStart);
                double dist_y = radius - y + y_offsetStart;
                double dist = Math.sqrt((dist_x * dist_x) + (dist_y * dist_y));
                if (radius >= dist)
                {
                    Color color = new Color(pixmap.getPixel(x, y));
                    if(dist > antiAliasBandry )
                        color.a = (float) ((radius-dist)/ANTI_ALIAS_WIDTH);
                    round.drawPixel(x - x_offsetStart, y - y_offsetStart, Color.rgba8888(color));
                }
                else
                {
                    round.drawPixel(x - x_offsetStart, y - y_offsetStart, 0);
                }
            }
        }
        Gdx.app.log("info", "pixmal rounded!");
        return round;
    }

}

