package io.oscr.androidchess;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import io.oscr.androidchess.controller.ChessController;
import io.oscr.androidchess.model.BoardPosition;
import io.oscr.androidchess.model.ChessModel;
import io.oscr.androidchess.model.IChessModel;
import io.oscr.androidchess.utils.Constants;

import static io.oscr.androidchess.utils.Constants.FILES;

public class MainActivity extends Activity implements PropertyChangeListener {

    private Button[][] board;
    private ChessController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up game and listeners
        IChessModel model = new ChessModel();
        controller = new ChessController(model);
        model.addObserver(this);

        board = new Button[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for(int i = 7; 0 <= i; i-- ){
            for(int j = 0; j <= 7; j++){

                // The following finds items using names and not id number. Makes code
                // more readable but is also more expensive operation!
                String name = FILES[j] + (i+1);
                int id = getResources().getIdentifier(name, "id", getPackageName());
                board[j][i] = (Button)findViewById(id);

                String pieceStr = controller.getPieceString(j, i);
                if(pieceStr != null){
                    id = getResources().getIdentifier(pieceStr, "drawable", getPackageName());
                    Drawable d = getResources().getDrawable(id);
                    LayerDrawable ld = new LayerDrawable(new Drawable[]{controller.getBoardColor(j, i), d});
                    board[j][i].setBackground(ld);
                } else {
                    // DO NOT REMOVE THE FOLLOWING LOGIC!!!!!!!
                    // If this is not performed the 4 middle rows will act strange when pieces are
                    // placed upon them. MANY, MANY, MANY hours of debugging found this solution.
                    LayerDrawable ld = new LayerDrawable(new Drawable[]{controller.getBoardColor(j, i)});
                    board[j][i].setBackground(ld);
                }
                board[j][i].setOnClickListener(new ButtonSelectionListener(j, i));
            }
        }
    }

    private void redrawBoard(){
        for(int i = 7; 0 <= i; i-- ){
            for(int j = 0; j <= 7; j++){
                board[j][i].setBackground(null);
                board[j][i].setBackgroundColor(controller.getBoardColor(j, i).getColor());

                String pieceStr = controller.getPieceString(j, i);
                if(pieceStr != null){
                    // Have to scale the images or it looks really fubar
                    int id = getResources().getIdentifier(pieceStr, "drawable", getPackageName());
                    Drawable d = getResources().getDrawable(id);
                    LayerDrawable ld = new LayerDrawable(new Drawable[]{controller.getBoardColor(j, i), d});
                    board[j][i].setBackground(ld);

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        redrawBoard();
        findViewById(android.R.id.content).invalidate();
    }

    private class ButtonSelectionListener implements View.OnClickListener {
        private final BoardPosition position;

        private ButtonSelectionListener(int file, int rank){
            position = new BoardPosition(file, rank);
        }

        @Override
        public void onClick(View view) {
            controller.selectPosition(position);
        }
    }
}
