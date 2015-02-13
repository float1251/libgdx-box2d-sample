package jp.float1251.box2dsample;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static jp.float1251.box2dsample.Constants.PIXEL_TO_METER;

public class Box2dSample extends ApplicationAdapter {
    SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private FitViewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // camera settings
        OrthographicCamera camera = new OrthographicCamera();
        // これで左下に原点が来るように調整するようになる。
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);

        debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, -9.8f), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        ChainShape shape = new ChainShape();
        shape.createChain(new float[]{
                100f / PIXEL_TO_METER, (Constants.SCREEN_HEIGHT - 100) / PIXEL_TO_METER,
                100f / PIXEL_TO_METER, 0f,
                (Constants.SCREEN_WIDTH - 100) / PIXEL_TO_METER, 0f,
                (Constants.SCREEN_WIDTH - 100) / PIXEL_TO_METER, (Constants.SCREEN_HEIGHT - 100) / PIXEL_TO_METER
        });
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.7f;
        fixtureDef.friction = 0.5f;
        body.createFixture(fixtureDef);

        createCircle();

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                switch (keycode) {
                    case Input.Keys.SPACE:
                        createCircle();
                        break;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });
    }

    private void createCircle() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        float x = (float) (Math.random() * 10);
        bdef.position.set((Constants.SCREEN_WIDTH / 2 + x) / PIXEL_TO_METER, Constants.SCREEN_HEIGHT / 2 / PIXEL_TO_METER);
        Body ballBody = world.createBody(bdef);

        CircleShape circle = new CircleShape();
        circle.setRadius(20f / PIXEL_TO_METER);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = circle;
        fdef.restitution = 0.4f;
        fdef.density = 0.3f;
        fdef.friction = 0.4f;
        ballBody.createFixture(fdef);

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.getCamera().update();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        debugRenderer.render(world, viewport.getCamera().combined.cpy().scl(PIXEL_TO_METER));
        batch.begin();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

}
