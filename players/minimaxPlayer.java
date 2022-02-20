/*
CM3112 Task 1, James Blue C1816427
Due to an issue regarding the movement of the snake (or lack of) for my minimaxPlayer and time constraints, I was
unable to successfully run the system and allow the snakes to complete a game, with the issue likely being within the
move function of the system.
Because of this it is assumed that the player would have won 0 of the 20 matches, as even with implementing a move it
was unable to successfully move anywhere but up and kill itself.
The system was given 100ms within the move to be able to complete its move, and within my code there is the successful
functions of initialising the positions within the board and finding the available moves to the snake, and an attempt
to get a utility value by using manhattanDistance which did output the successful result.
There is also the unsuccessful attempt of getting the snake to move based off of these values.
I did also attempt to properly implement a minimum and maximum value by making a minimum distance utilising the
euclidean distance to closest, but this was lost due to corruption and because of a lack of time I was unable to
properly implement these, this version being the closest I had of a complete system.
 */


package players;


import snake.GameState;
import snake.Snake;

import java.util.*;

public class minimaxPlayer extends SnakePlayer {

    public minimaxPlayer(GameState state, int index, Snake game) {
        super(state, index, game);
    }

    public void doMove(){
        initPositions();
        /* Statement to check whether the target disk has been eaten, if this is the case then this will
        trigger the chance node. Due to issues of implementing the minimax system I was unable to fully implement
        a chance node system.
        This would have been initiated with a statement
        if (state.hasTarget() == true) {
            functionRelatedToChanceBlock();
        }
        */

        /* In this case, the system will find a move for the index of the current position of the snake and then set the orientation
        to this, allowing it to move in the given direction.
         */
        Node n = findMove(index);
        state.setOrientation(index, n.getFirstMove());

    }



    /* It is important to initialise positions within the game to ensure that the AI can then tell whether there
    is a user occupying a certain space to ensure it doesn't hit into it and die, as well as finding the optimal
    path to the target.
     */
    Map<Position, Integer> positions;
    private void initPositions() {
        positions = new HashMap();
        for (int i = 0; i < state.getNrPlayers(); i++) {
            if (!state.isDead(i)) {
                List<Integer> xList = state.getPlayerX(i);
                List<Integer> yList = state.getPlayerY(i);
                for (int j = 0; j < xList.size(); j++) {
                    positions.put(new Position(xList.get(j), yList.get(j)), xList.size() - j);
                }
            }
        }
    }


    private Node lastMove = null;
    private Node findMove(int p) {
        int s = state.getLastOrientation(p);
        long timeUp = System.currentTimeMillis() + 100;
        Node lastBest = null;
        List<Node> moveList = availableMoves(p);
        System.out.println(moveList);
        for (; System.currentTimeMillis() < timeUp; timeUp++) {
            Node bestMove = null;
            double bestScore = 0;
            for (Node m : moveList) {
                /* Node m within the move list is then measured for the distance between it and the target by using
                euclidean distance to closest, with the closest being the target and original being the node.
                Due to time constraints, this was used to evaluate moves and there is not any depth implemented within
                the system.
                 */
                double score = manhattanDistance(m);
                /* if statement ensures that within the time specified the player will make their given move.
                 */
                if (System.currentTimeMillis() > timeUp) {
                    break;
                }
                /* Checks the utility of the value and if it is better than the current value, aka is closer than it,
                then it will be used.
                 */
                if (bestMove == null || score < bestScore) {
                    bestMove = m;
                    bestScore = score;
                }
            }
            if (System.currentTimeMillis() < timeUp)
                lastBest = bestMove;
            }
        System.out.println(lastBest);
        /* lastMove is then set to lastBest and this is the outputted value to set the orientation of the snake to.
         */
        lastMove = lastBest;
        return lastMove;
    }


    /* To find the optimal move, in each state it is useful to create a list of moves available to the snake
    and then these moves can then be evaluated to a certain depth.
     */
    public List<Node> availableMoves(int ind) {
        List<Node> options = new ArrayList();
        for (int i = 1; i <= 4; i++) {
            if (state.isLegalMove(index, i)) {
                if (i == 1) {
                    options.add(new Node(state.getPlayerX(index).get(0), state.getPlayerY(index).get(0)+1, state.getTargetX(), state.getTargetY()));
                } else if (i == 2) {
                    options.add(new Node(state.getPlayerX(index).get(0)+1, state.getPlayerY(index).get(0), state.getTargetX(), state.getTargetY()));
                } else if (i == 3) {
                    options.add(new Node(state.getPlayerX(index).get(0), state.getPlayerY(index).get(0)-1, state.getTargetX(), state.getTargetY()));
                } else if (i == 4) {
                    options.add(new Node(state.getPlayerX(index).get(0)-1, state.getPlayerY(index).get(0), state.getTargetX(), state.getTargetY()));
                }
            }
        }
        return options;
    }

    /* Manhattan distance used to calculate the closest distance to the target node.
    *** USED LAB EXERCISE FUNCTIONS FOR THIS ***
     */

    public double manhattanDistance(Node m) {
        return Math.abs(m.x-state.getTargetX())+Math.abs(m.y-state.getTargetY());
    }

}
