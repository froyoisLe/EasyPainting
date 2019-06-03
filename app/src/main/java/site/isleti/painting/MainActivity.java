package site.isleti.painting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import java.io.ByteArrayOutputStream;

import site.isleti.painting.service.MusicService;

import static android.view.View.GONE;

public class MainActivity extends Activity {

    private ImageView pen;
    private ImageView penInactive;
    private ImageView pencil;
    private ImageView pencilInactive;
    private ImageView erase;
    private ImageView eraseInactive;
    private ImageView mark;
    private ImageView markInactive;
    private ImageView chooseColor;
    private LinearLayout showColor;
    private ImageView choosePattern;
    private LinearLayout showPattern;
    private ImageButton currPaint;
    private DrawingView drawView;
    private ImageView deleteBtn;
    private ImageButton refreshTip;
    private TextView tip;
    private ImageButton warnWhite;
    private ImageButton warnBlack;
    private LinearLayout mTipLin;
    private int count = 0;
    private static final String TAG = "画板";
    private String currentPen = "";
    private ImageButton done;
    private Bitmap mBitmap;
    private FrameLayout tipWindow;
    //退出判断
    private boolean isQuit = false;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = findViewById(R.id.drawing);
        mIntent = new Intent(MainActivity.this, MusicService.class);
        penChoose();
        showTip();
        clearDrawing();
        tipRefresh();
        LinearLayout paintLayout = findViewById(R.id.pick_color);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setBackground(getResources().getDrawable(R.drawable.circle));
        drawView.setBrushSize(24);
        currentPen = "mark";
        drawView.setPaintAlpha(80);
        save();
        musicPlay();
        hideTip();
    }

    private void hideTip() {
        tipWindow = findViewById(R.id.tip_window);
        tipWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipWindow.setVisibility(GONE);
            }
        });
    }
    private void musicPlay() {
        startService(mIntent);
    }
    private void save() {
        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setDrawingCacheEnabled(true);
                mBitmap = drawView.getDrawingCache();
                //Drawable mDrawable = new BitmapDrawable(mBitmap);
                byte[] bytes = bitmap2Bytes(mBitmap);
                Intent intent = new Intent(MainActivity.this,SaveActvity.class);
                intent.putExtra("image",bytes);
                startActivity(intent);
                Log.d(TAG, "onClick: 携带图片跳转");
                drawView.setDrawingCacheEnabled(false);//禁用避免性能受影响
            }
        });
    }
    private byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private void showTip() {
        mTipLin = findViewById(R.id.tip);
        warnBlack = findViewById(R.id.warn_black);
        warnWhite = findViewById(R.id.warn_white);
        warnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warnWhite.setVisibility(GONE);
                warnBlack.setVisibility(View.VISIBLE);
                mTipLin.setVisibility(View.INVISIBLE);
            }
        });
        warnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warnWhite.setVisibility(View.VISIBLE);
                warnBlack.setVisibility(GONE);
                mTipLin.setVisibility(View.VISIBLE);
            }
        });
    }
    private void tipRefresh() {
        refreshTip = findViewById(R.id.refresh);
        tip = findViewById(R.id.tip_text);
        refreshTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count ++;
                int tag = count % 10;
                String [] tips = {
                       "今天我要做什么呢哈哈哈哈",
                       "在操场我最喜欢做的事",
                        "如果昆虫成为巨人会怎样",
                        "如果城市建在丛林里会怎样",
                        "如果大象会飞会怎样",
                        "我最喜欢的故事角色",
                        "为鳄鱼设计专属的地下隧道",
                        "萤火虫和星星的区别是什么",
                        "我最喜欢的颜色的场景",
                        "最近做的奇怪的梦...",
                };
                tip.setText(tips[tag]);

            }
        });

    }
    private void clearDrawing() {
        deleteBtn = findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(MainActivity.this);
                dialogBuilder
                         .withTitle("清空画板")
                        .withIcon(R.mipmap.tip_on)
                        .withTitleColor("#FFFCE9")
                        .withDividerColor("#E2C679")
                        .withMessage("   确定要删除当前画作？")                     //.withMessage(null)  no Msg
                        .withMessageColor("#FFFCE9")
                        .withDialogColor("#F0CB65")                               //def  | withDialogColor(int resid)
                        .withDuration(500)                                          //def
                        .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                        .withButton1Text("取消")                                      //def gone
                        .withButton2Text("确定")                                  //def gone
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.cancel();
                                Toast.makeText(v.getContext(), "已取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                drawView.startNew();
                                Toast.makeText(v.getContext(),"已删除",Toast.LENGTH_SHORT).show();
                                dialogBuilder.cancel();
                            }
                        })
                        .show();
            }
        });
    }
    private void penChoose() {
        penCase();
        pencilCase();
        markCase();
        eraseCase();
        chooseColor = findViewById(R.id.choose_color);
        showColor = findViewById(R.id.show_color);
        chooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showColor.getVisibility() == GONE) {
                    showColor.setVisibility(View.VISIBLE);
                    showPattern.setVisibility(GONE);
                } else
                    showColor.setVisibility(GONE);
            }
        });
        choosePattern = findViewById(R.id.choose_pattern);
        showPattern = findViewById(R.id.show_pattern);
        choosePattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPattern.getVisibility() == GONE) {
                    showPattern.setVisibility(View.VISIBLE);
                    showColor.setVisibility(GONE);
                } else showPattern.setVisibility(GONE);
            }
        });
    }
    private void eraseCase() {

        erase = findViewById(R.id.eraser_active);
        eraseInactive = findViewById(R.id.eraser_inactive);
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //erase.setVisibility(GONE);
                Log.d(TAG, "colorChoose:选擦透明度 " + drawView.getPaintAlpha());
                eraseAct();
                //eraseInactive.setVisibility(View.VISIBLE);
            }
        });
        eraseInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "colorChoose:选擦透明度 " + drawView.getPaintAlpha());
                eraseInactive.setVisibility(GONE);
                erase.setVisibility(View.VISIBLE);
                markInactive.setVisibility(View.VISIBLE);
                mark.setVisibility(GONE);
                pencil.setVisibility(GONE);
                pencilInactive.setVisibility(View.VISIBLE);
                penInactive.setVisibility(View.VISIBLE);
                pen.setVisibility(GONE);
                eraseAct();
            }
        });
    }
    private void eraseAct() {
                 Log.d(TAG, "colorChoose:选擦透明度 " + drawView.getPaintAlpha());
                drawView.setErase(true);
                drawView.setBrushSize(15);
    }
    private void markCase() {
        mark = findViewById(R.id.mark_active);
        markInactive = findViewById(R.id.mark_inactive);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPen = "mark";
                drawView.setPaintAlpha(80);
                Log.d(TAG, "colorChoose:mark透明度 " + drawView.getPaintAlpha());
                //mark.setVisibility(GONE);
                markAct();
                //markInactive.setVisibility(View.VISIBLE);
            }
        });
        markInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPen = "mark";
                drawView.setPaintAlpha(80);
                Log.d(TAG, "colorChoose:mark透明度 " + drawView.getPaintAlpha());
                markAct();
                markInactive.setVisibility(GONE);
                mark.setVisibility(View.VISIBLE);
                pencil.setVisibility(GONE);
                pencilInactive.setVisibility(View.VISIBLE);
                erase.setVisibility(GONE);
                eraseInactive.setVisibility(View.VISIBLE);
                penInactive.setVisibility(View.VISIBLE);
                pen.setVisibility(GONE);
            }
        });
    }
    private void markAct() {
        currentPen = "mark";
        drawView.setPaintAlpha(80);
        drawView.setErase(false);
        drawView.setBrushSize(24);
        drawView.setLastBrushSize(24);
    }
    private void pencilCase() {
        pencil = findViewById(R.id.pencil_active);
        pencilInactive = findViewById(R.id.pencil_inactive);
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPen = "pencil";
                drawView.setPaintAlpha(100);
                Log.d(TAG, "colorChoose:pencil透明度 " + drawView.getPaintAlpha());
                pencilAct();
               // pencil.setVisibility(GONE);
                //pencilInactive.setVisibility(View.VISIBLE);
            }
        });

        pencilInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPen = "pencil";
                drawView.setPaintAlpha(100);
                Log.d(TAG, "colorChoose:pencil透明度 " + drawView.getPaintAlpha());
                pencilInactive.setVisibility(GONE);
                pencil.setVisibility(View.VISIBLE);
                mark.setVisibility(GONE);
                markInactive.setVisibility(View.VISIBLE);
                erase.setVisibility(GONE);
                eraseInactive.setVisibility(View.VISIBLE);
                penInactive.setVisibility(View.VISIBLE);
                pen.setVisibility(GONE);
                pencilAct();
            }
        });
    }
    private void pencilAct() {
        currentPen = "pencil";
        drawView.setPaintAlpha(100);
        drawView.setErase(false);
        drawView.setBrushSize(8);
        drawView.setLastBrushSize(8);
    }
    private void penCase() {
        pen = findViewById(R.id.pen_active);
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPen = "pen";
                drawView.setPaintAlpha(100);
                penAct();
                //penInactive.setVisibility(View.VISIBLE);
                //pen.setVisibility(GONE);
            }
        });

        penInactive = findViewById(R.id.pen_inactive);
        penInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPen = "pen";
                drawView.setPaintAlpha(100);
                penAct();
                penInactive.setVisibility(GONE);
                pen.setVisibility(View.VISIBLE);
                pencil.setVisibility(GONE);
                pencilInactive.setVisibility(View.VISIBLE);
                mark.setVisibility(GONE);
                markInactive.setVisibility(View.VISIBLE);
                erase.setVisibility(GONE);
                eraseInactive.setVisibility(View.VISIBLE);

            }
        });
    }
    private void penAct() {
        currentPen = "pen";
        drawView.setPaintAlpha(85);
        drawView.setErase(false);
        drawView.setBrushSize(3);
        drawView.setLastBrushSize(3);
        drawView.setPaintAlpha(100);
    }
    private void setBrushAlpha() {
        String currentPen = this.currentPen;
        Log.d(TAG, "colorChoose:现在的笔 " + currentPen);
        switch (currentPen) {
            case "pen":
                drawView.setPaintAlpha(100);
                break;
            case "mark":
                drawView.setPaintAlpha(80);
                break;
            case "pencil":
                drawView.setPaintAlpha(100);
                break;
        }
        Log.d(TAG, "colorChoose:笔的透明度 " + drawView.getPaintAlpha());
    }
    public void colorChoose(View view) {
        //use chosen color
        //set erase false
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        String currentPen = this.currentPen;
        Log.d(TAG, "colorChoose:现在的笔 " + currentPen);
        switch (currentPen) {
            case "pen":
                drawView.setPaintAlpha(100);
                break;
            case "mark":
                drawView.setPaintAlpha(80);
                break;
            case "pencil":
                drawView.setPaintAlpha(100);
                break;
        }
        Log.d(TAG, "colorChoose:笔的透明度 " + drawView.getPaintAlpha());
        if (view != currPaint) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //update ui
            imgView.setBackground(getResources().getDrawable(R.drawable.circle));
            currPaint.setBackground(getResources().getDrawable(R.drawable.circle_inactive));
            currPaint = (ImageButton) view;
            Log.d(TAG, "colorChoose: 选中颜色" + color);
            setBrushAlpha();
            chooseColor.setImageDrawable(currPaint.getDrawable());
            showColor.setVisibility(GONE);
            showPattern.setVisibility(GONE);
        }
    }
    public void patternChoose(View view) {
        drawView.setErase(false);
        drawView.setPaintAlpha(100);
        drawView.setBrushSize(drawView.getLastBrushSize());
        String currentPen = this.currentPen;
        Log.d(TAG, "colorChoose:现在的笔 " + currentPen);
        switch (currentPen) {
            case "pen":
                drawView.setPaintAlpha(100);
                break;
            case "mark":
                drawView.setPaintAlpha(80);
                break;
            case "pencil":
                drawView.setPaintAlpha(100);
                break;
        }
        Log.d(TAG, "colorChoose:笔的透明度 " + drawView.getPaintAlpha());
        if (view != currPaint) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //update ui
            imgView.setBackground(getResources().getDrawable(R.drawable.circle));
            currPaint.setBackground(getResources().getDrawable(R.drawable.circle_inactive));
            currPaint = (ImageButton) view;
            Log.d(TAG, "colorChoose: 选中颜色" + color);
            setBrushAlpha();
            choosePattern.setImageDrawable(currPaint.getDrawable());
            showColor.setVisibility(GONE);
            showPattern.setVisibility(GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
    }

    @Override
    public void onBackPressed() {
        if (!isQuit) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            isQuit = true;
            //两秒钟之后isQuit会变成false
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        isQuit = false;
                    }
                }
            }).start();
        } else {
            this.finish();
        }
    }
}
