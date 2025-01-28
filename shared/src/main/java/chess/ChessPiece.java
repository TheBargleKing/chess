package chess;

import java.util.ArrayList;
import java.util.Collection;



/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        int result = pieceColor != null ? pieceColor.hashCode() : 0;
        result = 31 * result + (pieceType != null ? pieceType.hashCode() : 0);
        return result;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch (getPieceType()) {
            case PAWN:
                if (legalLocation(myPosition))  {
                    if (getTeamColor() == ChessGame.TeamColor.WHITE) {
                        ChessPosition end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                        if (legalLocation(end) && board.getPiece(end) == null) {
                            if (myPosition.getRow() == 7) {
                                ChessMove move = new ChessMove(myPosition, end, pieceType.QUEEN );
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.KNIGHT);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.BISHOP);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.ROOK);
                                moves.add(move);
                            } else {
                                ChessMove move = new ChessMove(myPosition, end, null);
                                moves.add(move);
                            }
                            if (myPosition.getRow() == 2) {
                                end = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                                if (legalLocation(end) && board.getPiece(end) == null) {
                                    ChessMove move = new ChessMove(myPosition, end, null);
                                    moves.add(move);
                                }
                            }
                        }
                        end = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                        if (legalLocation(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != getTeamColor()) {
                            if (myPosition.getRow() == 7) {
                                ChessMove move = new ChessMove(myPosition, end, pieceType.QUEEN);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.KNIGHT);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.BISHOP);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.ROOK);
                                moves.add(move);
                            } else {
                                ChessMove move = new ChessMove(myPosition, end, null);
                                moves.add(move);
                            }
                        }
                        end = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                        if (legalLocation(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != getTeamColor()) {
                            if (myPosition.getRow() == 7) {
                                ChessMove move = new ChessMove(myPosition, end, pieceType.QUEEN);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.KNIGHT);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.BISHOP);
                                moves.add(move);
                                move = new ChessMove(myPosition, end, pieceType.ROOK);
                                moves.add(move);
                            } else {
                                ChessMove move = new ChessMove(myPosition, end, null);
                                moves.add(move);
                            }
                        }
                    } else if (legalLocation(myPosition))  {
                        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                            ChessPosition end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                            if (legalLocation(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != getTeamColor()) {
                                if (myPosition.getRow() == 2) {
                                    ChessMove move = new ChessMove(myPosition, end, pieceType.QUEEN);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.KNIGHT);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.BISHOP);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.ROOK);
                                    moves.add(move);
                                } else {
                                    ChessMove move = new ChessMove(myPosition, end, null);
                                    moves.add(move);
                                }
                            }
                            end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                            if (legalLocation(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != getTeamColor()) {
                                if (myPosition.getRow() == 2) {
                                    ChessMove move = new ChessMove(myPosition, end, pieceType.QUEEN);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.KNIGHT);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.BISHOP);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.ROOK);
                                    moves.add(move);
                                } else {
                                    ChessMove move = new ChessMove(myPosition, end, null);
                                    moves.add(move);
                                }
                            }
                            end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                            if (legalLocation(end) && board.getPiece(end) == null) {
                                if (myPosition.getRow() == 2) {
                                    ChessMove move = new ChessMove(myPosition, end, pieceType.QUEEN );
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.KNIGHT);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.BISHOP);
                                    moves.add(move);
                                    move = new ChessMove(myPosition, end, pieceType.ROOK);
                                    moves.add(move);
                                } else {
                                    ChessMove move = new ChessMove(myPosition, end, null);
                                    moves.add(move);
                                }
                                if (myPosition.getRow() == 7) {
                                    end = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                                    if (legalLocation(end) && board.getPiece(end) == null) {
                                        ChessMove move = new ChessMove(myPosition, end, null);
                                        moves.add(move);
                                    }
                                }
                            }
                        }
                    }
                }
            case KNIGHT:
                break;
            case BISHOP:
                break;
            case ROOK:
                break;
            case QUEEN:
        }
        return moves;
    }
    public boolean legalLocation(ChessPosition position) {
        if (position.getColumn() > 8 || position.getColumn() < 1) {
            return false;
        } else return position.getRow() <= 8 && position.getRow() >= 1;
    }
}
