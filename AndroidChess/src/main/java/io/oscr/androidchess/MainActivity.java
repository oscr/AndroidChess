package io.oscr.androidchess;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up game and listeners
        IChessModel model = new ChessModel();
        ChessController controller = new ChessController(model);
        model.addObserver(this);




        board = new Button[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for(int i = 7; 0 <= i; i-- ){
            for(int j = 0; j <= 7; j++){
                // The following finds items using names and not id number. Makes code
                // more readable but is also more expensive operation!
                String name = FILES[j] + (i+1);
                int id = getResources().getIdentifier(name, "id", getPackageName());

                board[j][i] = (Button)findViewById(id);
                board[j][i].setBackgroundColor(controller.getBoardColor(j, i));

                String pieceStr = controller.getPieceString(j, i);
                if(pieceStr != null){
                    id = getResources().getIdentifier("bp", "drawable", getPackageName());
                    Log.d("Id", "" + id);

                    Drawable d = getResources().getDrawable(id);
                    Log.d("NULL D:", ""+(d == null));

                    board[j][i].setCompoundDrawablesWithIntrinsicBounds(null, d, null, new ColorDrawable(controller.getBoardColor(j, i)));
                }

                // TODO
                //board[j][i].setOnClickListener();
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

    }
}
