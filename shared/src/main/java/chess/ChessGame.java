package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGame {
    private ChessBoard gameBoard;
    private TeamColor teamTurn;

    public ChessGame() {
        this.gameBoard = new ChessBoard();
        this.gameBoard.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (startPosition == null || gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : piece.pieceMoves(gameBoard, startPosition)) {
            if (!isInCheckAfterMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private boolean isInCheckAfterMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessBoard simulatedBoard = cloneBoard(gameBoard);
        simulatedBoard.addPiece(move.getEndPosition(), simulatedBoard.getPiece(start));
        simulatedBoard.addPiece(start, null);

        TeamColor teamColor = simulatedBoard.getPiece(move.getEndPosition()).getTeamColor();
        return isInCheck(teamColor, simulatedBoard);
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (move == null) {
            throw new InvalidMoveException("Invalid Move: Move cannot be null.");
        }
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = gameBoard.getPiece(start);
        if (movingPiece == null || teamTurn != movingPiece.getTeamColor()) {
            throw new InvalidMoveException("Invalid move: No piece at position or not your turn.");
        }
        if (!validMoves(start).contains(move)) {
            throw new InvalidMoveException("Invalid move: Move not valid.");
        }
        // Perform the move
        gameBoard.addPiece(start, null);
        if (move.getPromotionPiece() != null) {
            gameBoard.addPiece(end, new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
        } else {
            gameBoard.addPiece(end, movingPiece);
        }
        // Make sure not moving into check.
        if (isInCheck(teamTurn)) {
            throw new InvalidMoveException("Invalid move: Cannot move into check.");
        }
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, gameBoard);
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = findKingPosition(teamColor, board);
        if (kingPosition == null) {
            return false;
        }
        TeamColor enemyColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == enemyColor) {
                    for (ChessMove move : piece.pieceMoves(board, position)) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKingPosition(TeamColor teamColor, ChessBoard board) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    for (ChessMove move : validMoves(position)) {
                        return false; // If at least one move gets out of check, it's not checkmate
                    }
                }
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(position).isEmpty()) {
                        return false; // If at least one move is possible, it's not stalemate
                    }
                }
            }
        }
        return true;
    }

    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    public ChessBoard getBoard() {
        return gameBoard;
    }

    public ChessBoard cloneBoard(ChessBoard board) {
        ChessBoard newBoard = new ChessBoard();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    newBoard.addPiece(position, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }
        return newBoard;
    }
}
