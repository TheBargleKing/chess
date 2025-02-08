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
        WHITE, BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if (startPosition == null || gameBoard.getPiece(startPosition) == null) {
            return validMoves;
        }
        ChessPiece piece = gameBoard.getPiece(startPosition);
        for (ChessMove move : piece.pieceMoves(gameBoard, startPosition)) {
            if (!isInCheckAfterMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private boolean isInCheckAfterMove(ChessMove move) {
        if (gameBoard.getPiece(move.getStartPosition()).getPieceType()  == ChessPiece.PieceType.KING) {
            if (isInCheckAfterKingMove(move)) {
                return true;
            }
        }
        ChessBoard simulatedBoard = cloneBoard(gameBoard);
        ChessPiece piece = simulatedBoard.getPiece(move.getStartPosition());
        simulatedBoard.addPiece(move.getEndPosition(), piece);
        simulatedBoard.addPiece(move.getStartPosition(), null);
        return isInCheck(piece.getTeamColor(), simulatedBoard);
    }

    private boolean isInCheckAfterKingMove(ChessMove move) {
        ChessBoard simulatedBoard = cloneBoard(gameBoard);
        ChessPiece piece = simulatedBoard.getPiece(move.getStartPosition());
        TeamColor enemyColor = (piece.getTeamColor() == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        simulatedBoard.addPiece(move.getStartPosition(), null);
        simulatedBoard.addPiece(move.getEndPosition(), piece);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece attackingPiece = simulatedBoard.getPiece(position);
                if (attackingPiece != null && attackingPiece.getTeamColor() == enemyColor) {
                    for (ChessMove checkMove : attackingPiece.pieceMoves(simulatedBoard, position)) {
                        if (checkMove.getEndPosition().equals(move.getEndPosition())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (move == null) {
            throw new InvalidMoveException("Invalid Move: Move cannot be null.");
        }
        ChessPiece movingPiece = gameBoard.getPiece(move.getStartPosition());
        if (movingPiece == null || teamTurn != movingPiece.getTeamColor()) {
            throw new InvalidMoveException("Invalid move: No piece at position or not your turn.");
        }
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move: Move not valid.");
        }

        // Perform the move
        gameBoard.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            gameBoard.addPiece(move.getEndPosition(), new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
        } else {
            gameBoard.addPiece(move.getEndPosition(), movingPiece);
        }

        // Ensure the move doesn't leave the king in check
        if (isInCheck(teamTurn)) {
            throw new InvalidMoveException("Invalid move: Cannot move into check.");
        }

        // Switch turns
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, gameBoard);
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = findKingPosition(teamColor, board);
        if (kingPosition == null) return false;

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
        if (!isInCheck(teamColor)) return false;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor && !validMoves(position).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor && !validMoves(position).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    public ChessBoard getBoard() {
        return gameBoard;
    }

    private ChessBoard cloneBoard(ChessBoard board) {
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