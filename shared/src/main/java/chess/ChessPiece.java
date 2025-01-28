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
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = pieceColor.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return type;
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
        ChessPosition end;
        switch (type) {
            case KING:
                if (myPosition.getRow() + 1 <= 8) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 1 <= 8) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 1 >= 1) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 1 <= 8) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 1 >= 1) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getColumn() + 1 <= 8) {
                    end = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getColumn() - 1 >= 1) {
                    end = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() - 1 >= 1) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                break;
            case QUEEN:
                if (myPosition.getRow() < 8 && myPosition.getColumn() < 8) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8 && end.getColumn() < 8) {
                            end = new ChessPosition(end.getRow() + 1, end.getColumn() + 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() < 8 && myPosition.getColumn() > 1) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8 && end.getColumn() > 1) {
                            end = new ChessPosition(end.getRow() + 1, end.getColumn() - 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1 && myPosition.getColumn() > 1) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1 && end.getColumn() > 1) {
                            end = new ChessPosition(end.getRow() - 1, end.getColumn() - 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1 && myPosition.getColumn() < 8) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1 && end.getColumn() < 8) {
                            end = new ChessPosition(end.getRow() - 1, end.getColumn() + 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getColumn() < 8) {
                    end = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8) {
                            end = new ChessPosition(end.getRow(), end.getColumn() + 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1) {
                    end = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1) {
                            end = new ChessPosition(end.getRow(), end.getColumn() - 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1) {
                            end = new ChessPosition(end.getRow() - 1, end.getColumn());
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() < 8) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8) {
                            end = new ChessPosition(end.getRow() + 1, end.getColumn());
                        } else {
                            break;
                        }
                    }
                }
                break;
            case ROOK:
                if (myPosition.getColumn() < 8) {
                    end = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8) {
                            end = new ChessPosition(end.getRow(), end.getColumn() + 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1) {
                    end = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1) {
                            end = new ChessPosition(end.getRow(), end.getColumn() - 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1) {
                            end = new ChessPosition(end.getRow() - 1, end.getColumn());
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() < 8) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8) {
                            end = new ChessPosition(end.getRow() + 1, end.getColumn());
                        } else {
                            break;
                        }
                    }
                }
                break;
            case BISHOP:
                if (myPosition.getRow() < 8 && myPosition.getColumn() < 8) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8 && end.getColumn() < 8) {
                            end = new ChessPosition(end.getRow() + 1, end.getColumn() + 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() < 8 && myPosition.getColumn() > 1) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() < 8 && end.getColumn() > 1) {
                            end = new ChessPosition(end.getRow() + 1, end.getColumn() - 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1 && myPosition.getColumn() > 1) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1 && end.getColumn() > 1) {
                            end = new ChessPosition(end.getRow() - 1, end.getColumn() - 1);
                        } else {
                            break;
                        }
                    }
                }
                if (myPosition.getRow() > 1 && myPosition.getColumn() < 8) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    while (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                        if (board.getPiece(end) != null) {
                            break;
                        }
                        if (end.getRow() > 1 && end.getColumn() < 8) {
                            end = new ChessPosition(end.getRow() - 1, end.getColumn() + 1);
                        } else {
                            break;
                        }
                    }
                }
                break;
            case KNIGHT:
                if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() + 1 <= 8) {
                    end = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() - 1 >= 1) {
                    end = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() + 1 <= 8) {
                    end = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() - 1 >= 1) {
                    end = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 2 <= 8) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 2 >= 1) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 2 <= 8) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 2 >= 1) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
                    if (checkBounds(end) && (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor)) {
                        moves.add(new ChessMove(myPosition, end, null));
                    }
                }
                break;
            case PAWN:
                // WHITE PAWN
                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (checkBounds(end) && board.getPiece(end) == null) {
                        if (myPosition.getRow() == 7) {
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(myPosition, end, null));
                            if (myPosition.getRow() == 2) {
                                end = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                                if (checkBounds(end) && board.getPiece(end) == null) {
                                    moves.add(new ChessMove(myPosition, end, null));
                                }
                            }
                        }
                    }
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if (checkBounds(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != pieceColor) {
                        if (myPosition.getRow() == 7) {
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(myPosition, end, null));
                        }
                    }
                    end = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if (checkBounds(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != pieceColor) {
                        if (myPosition.getRow() == 7) {
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(myPosition, end, null));
                        }
                    }
                    // BLACK PAWN
                } else if (pieceColor == ChessGame.TeamColor.BLACK) {
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (checkBounds(end) && board.getPiece(end) == null) {
                        if (myPosition.getRow() == 2) {
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(myPosition, end, null));
                            if (myPosition.getRow() == 7) {
                                end = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                                if (checkBounds(end) && board.getPiece(end) == null) {
                                    moves.add(new ChessMove(myPosition, end, null));
                                }
                            }
                        }
                    }
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if (checkBounds(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != pieceColor) {
                        if (myPosition.getRow() == 2) {
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(myPosition, end, null));
                        }
                    }
                    end = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if (checkBounds(end) && board.getPiece(end) != null && board.getPiece(end).getTeamColor() != pieceColor) {
                        if (myPosition.getRow() == 2) {
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, end, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(myPosition, end, null));
                        }
                    }
                }
                break;


        }


        return moves;
    }
    public boolean checkBounds(ChessPosition position) {
        if (position.getColumn() > 8 || position.getColumn() < 1) {
            return false;
        } else return position.getRow() <= 8 || position.getRow() >= 1;
    }
}
