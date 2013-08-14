package io.oscr.androidchess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import io.oscr.androidchess.controller.ChessController;
import io.oscr.androidchess.model.BoardPosition;
import io.oscr.androidchess.model.ChessModel;
import io.oscr.androidchess.model.IChessModel;
import io.oscr.androidchess.model.event.ChessEvent;
import io.oscr.androidchess.model.event.PromotionEvent;
import io.oscr.androidchess.model.event.RedrawAllEvent;
import io.oscr.androidchess.model.event.RedrawEvent;
import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;
import io.oscr.androidchess.utils.Constants;

import static io.oscr.androidchess.utils.Constants.FILES;

/**
 * Represents the View in the MVC model.
 *
 * The view implements PropertyChangeListener in order to receive notifications from the model when
 * changes occur as part of the Observer pattern.
 */
public class MainActivity extends Activity implements PropertyChangeListener {
    /**
     * Matrix containing the whole chessboard represented as buttons.
     */
    private Button[][] board;

    /**
     * Reference to the Controller in the MVC pattern.
     */
    private ChessController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * I would have loved to have a factory perform the setup of model, view and controller
         * relations. However I'm unsure how to do this on the Android platform. So as ugly as it
         * is it's performed here. Adds the view as a listener to the ChessModel.
         */
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

                // If there is a Chess Piece on that position
                String pieceStr = controller.getPieceString(j, i);
                if(pieceStr != null){
                    // Obtains the drawable piece image resource
                    id = getResources().getIdentifier(pieceStr, "drawable", getPackageName());
                    Drawable d = getResources().getDrawable(id);
                    // Create a combined drawable containing both background and piece.
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
        TextView tv = (TextView)findViewById(R.id.information);
        tv.setText(controller.getDisplayInformation());
    }

    /**
     * Redraws the whole chessboard.
     */
    private void redrawBoard(){
        for(int i = 7; 0 <= i; i-- ){
            for(int j = 0; j <= 7; j++){
                board[j][i].setBackground(null);
                board[j][i].setBackgroundColor(controller.getBoardColor(j, i).getColor());

                // If there is a Chess Piece on that position
                String pieceStr = controller.getPieceString(j, i);
                if(pieceStr != null){
                    // Obtains the drawable piece image resource
                    int id = getResources().getIdentifier(pieceStr, "drawable", getPackageName());
                    Drawable d = getResources().getDrawable(id);
                    // Create a combined drawable containing both background and piece.
                    LayerDrawable ld = new LayerDrawable(new Drawable[]{controller.getBoardColor(j, i), d});
                    board[j][i].setBackground(ld);
                }
            }
        }
    }

    /**
     * Allows the redrawing of a arbitrary number of buttons instead of having to redraw the whole
     * board. This has proved to be somewhat more efficient than just redrawing everything.
     *
     * @param positions to be redrawn
     */
    private void redrawBoardPositions(final BoardPosition[] positions){
        for(BoardPosition bp : positions){
            final int file = bp.getFile();
            final int rank = bp.getRank();
            board[file][rank].setBackground(null);
            board[file][rank].setBackgroundColor(controller.getBoardColor(file, rank).getColor());

            // If there is a Chess Piece on that position
            String pieceStr = controller.getPieceString(file, rank);
            if(pieceStr != null){
                // Obtains the drawable piece image resource
                int id = getResources().getIdentifier(pieceStr, "drawable", getPackageName());
                Drawable d = getResources().getDrawable(id);
                // Create a combined drawable containing both background and piece.
                LayerDrawable ld = new LayerDrawable(new Drawable[]{controller.getBoardColor(file, rank), d});
                board[file][rank].setBackground(ld);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_change_theme:
                controller.changeColorTheme();
                break;
            case R.id.action_new_game:
                controller.newGame(PieceColor.WHITE);
                break;
        }
        return true;
    }

    /**
     * When the model has changed or the model needs input (as in the case of Pawn promotion)
     * a PropertyChangeEvent is sent to all observers. Here the behavior of the View is specified.
     *
     * @param propertyChangeEvent to handle.
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        // The event should be of type ChessEvent otherwise something strange is
        Object event = propertyChangeEvent.getNewValue();
        if(event instanceof ChessEvent){
            // Handles Pawn promotion
            if(event.getClass() == PromotionEvent.class){
                final PromotionEvent pe = (PromotionEvent)event;
                CharSequence[] types = {PieceType.QUEEN.toString(), PieceType.ROOK.toString(), PieceType.BISHOP.toString(), PieceType.KNIGHT.toString()};

                // Build the dialog and handle the input from user
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Promote pawn to");
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                controller.setPromotion(PieceType.QUEEN, pe);
                                break;
                            case 1:
                                controller.setPromotion(PieceType.ROOK, pe);
                                break;
                            case 2:
                                controller.setPromotion(PieceType.BISHOP, pe);
                                break;
                            case 3:
                                controller.setPromotion(PieceType.KNIGHT, pe);
                                break;
                        }
                    }
                });
                builder.show();

            // Only a few buttons need to be redrawn.
            } else if(event.getClass() == RedrawEvent.class){
                RedrawEvent re = (RedrawEvent)event;
                redrawBoardPositions(re.positions);

            // Whole board needs to be redrawn.
            } else if(event.getClass() == RedrawAllEvent.class){
                redrawBoard();
            }
            TextView tv = (TextView)findViewById(R.id.information);
            tv.setText(controller.getDisplayInformation());

        // If we got some other event than ChessEvent log for later inspection.
        } else {
            Log.d("Unknown event", event.toString());
        }
    }

    /**
     * Listener class for buttons. Each has it's own BoardPosition that is passed to the controller
     * as reference in order to identify what button was pushed.
     */
    private class ButtonSelectionListener implements View.OnClickListener {
        // What position on the board the button represents.
        private final BoardPosition position;

        /**
         * Creates a button selection listener with internal BoardPosition to represent what
         * square on the board the button is.
         *
         * @param file zero indexed file.
         * @param rank zero indexed rank.
         */
        private ButtonSelectionListener(int file, int rank){
            position = new BoardPosition(file, rank);
        }

        @Override
        public void onClick(View view) {
            controller.selectPosition(position);
        }
    }
}