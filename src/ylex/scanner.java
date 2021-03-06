package ylex;

/* The following code was generated by JFlex 1.6.1 */

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>Pascal.flex</tt>
 */
class scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\5\1\4\1\10\1\10\1\4\22\0\1\6\1\0\1\11"+
    "\4\0\1\7\1\41\1\42\1\52\1\46\1\43\1\47\1\2\1\53"+
    "\12\1\1\44\1\40\1\50\1\45\1\51\2\0\32\3\1\36\1\0"+
    "\1\37\3\0\1\16\1\34\1\33\1\32\1\24\1\21\1\15\1\23"+
    "\1\20\2\3\1\26\1\17\1\25\1\14\1\12\1\35\1\13\1\27"+
    "\1\22\1\30\1\56\1\31\1\54\1\55\1\3\12\0\1\10\u1fa2\0"+
    "\1\10\1\10\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\udfe6\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\4\2\0"+
    "\21\3\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
    "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\3"+
    "\1\0\1\24\1\0\1\25\2\3\1\26\1\27\7\3"+
    "\1\30\5\3\1\31\12\3\1\32\5\3\1\33\1\34"+
    "\1\35\1\36\1\3\1\37\1\40\5\3\1\41\1\42"+
    "\2\3\1\43\1\44\1\45\2\3\1\46\5\3\1\47"+
    "\1\3\1\50\1\3\1\51\6\3\1\52\3\3\1\53"+
    "\2\3\1\54\1\3\1\55\1\56\1\3\1\57\5\3"+
    "\1\60\1\61\1\62\1\63\1\64\1\65\5\3\1\66"+
    "\1\67\6\3\1\70\2\3\1\71\1\3\1\72\1\73"+
    "\1\74\1\3\1\75\1\3\1\76\2\3\1\77\1\100"+
    "\1\101\3\3\1\102\1\3\1\103\1\3\1\104\1\3"+
    "\1\105\1\106\1\3\1\107\1\110";

  private static int [] zzUnpackAction() {
    int [] result = new int[185];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\57\0\136\0\215\0\274\0\353\0\u011a\0\u0149"+
    "\0\u0178\0\u01a7\0\u01d6\0\u0205\0\u0234\0\u0263\0\u0292\0\u02c1"+
    "\0\u02f0\0\u031f\0\u034e\0\u037d\0\u03ac\0\u03db\0\u040a\0\u0439"+
    "\0\u0468\0\u0497\0\u04c6\0\u04c6\0\u04c6\0\u04c6\0\u04c6\0\u04c6"+
    "\0\u04f5\0\u04c6\0\u04c6\0\u04c6\0\u0524\0\u0553\0\u04c6\0\u04c6"+
    "\0\u0582\0\u05b1\0\u04c6\0\u05e0\0\u0178\0\u060f\0\u063e\0\u066d"+
    "\0\215\0\u069c\0\u06cb\0\u06fa\0\u0729\0\u0758\0\u0787\0\u07b6"+
    "\0\215\0\u07e5\0\u0814\0\u0843\0\u0872\0\u08a1\0\215\0\u08d0"+
    "\0\u08ff\0\u092e\0\u095d\0\u098c\0\u09bb\0\u09ea\0\u0a19\0\u0a48"+
    "\0\u0a77\0\u0aa6\0\u0ad5\0\u0b04\0\u0b33\0\u0b62\0\u0b91\0\u04c6"+
    "\0\u04c6\0\u04c6\0\u04c6\0\u0bc0\0\u05b1\0\u04c6\0\u0bef\0\u0c1e"+
    "\0\u0c4d\0\u0c7c\0\u0cab\0\215\0\215\0\u0cda\0\u0d09\0\215"+
    "\0\215\0\215\0\u0d38\0\u0d67\0\215\0\u0d96\0\u0dc5\0\u0df4"+
    "\0\u0e23\0\u0e52\0\215\0\u0e81\0\215\0\u0eb0\0\u0edf\0\u0f0e"+
    "\0\u0f3d\0\u0f6c\0\u0f9b\0\u0fca\0\u0ff9\0\215\0\u1028\0\u1057"+
    "\0\u1086\0\215\0\u10b5\0\u10e4\0\215\0\u1113\0\215\0\215"+
    "\0\u1142\0\215\0\u1171\0\u11a0\0\u11cf\0\u11fe\0\u122d\0\215"+
    "\0\215\0\215\0\215\0\215\0\215\0\u125c\0\u128b\0\u12ba"+
    "\0\u12e9\0\u1318\0\215\0\215\0\u1347\0\u1376\0\u13a5\0\u13d4"+
    "\0\u1403\0\u1432\0\215\0\u1461\0\u1490\0\215\0\u14bf\0\215"+
    "\0\u14ee\0\215\0\u151d\0\215\0\u154c\0\215\0\u157b\0\u15aa"+
    "\0\215\0\215\0\215\0\u15d9\0\u1608\0\u1637\0\215\0\u1666"+
    "\0\215\0\u1695\0\215\0\u16c4\0\215\0\215\0\u16f3\0\215"+
    "\0\215";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[185];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\0\1\2\1\3\1\4\1\5\1\6\1\7\1\10"+
    "\1\0\1\11\1\12\1\13\1\14\1\15\1\16\1\17"+
    "\1\20\1\21\1\22\1\4\1\23\1\24\1\4\1\25"+
    "\1\26\1\27\1\30\1\31\1\32\1\4\1\33\1\34"+
    "\1\35\1\36\1\37\1\40\1\41\1\42\1\43\1\44"+
    "\1\45\1\46\1\47\1\50\2\4\1\51\1\0\1\2"+
    "\1\52\56\0\1\53\57\0\1\4\6\0\24\4\16\0"+
    "\3\4\4\0\1\5\1\0\1\5\55\0\2\6\54\0"+
    "\1\5\1\6\1\7\50\0\4\54\1\0\3\54\1\0"+
    "\46\54\4\11\1\0\3\11\1\0\1\55\45\11\3\0"+
    "\1\4\6\0\1\4\1\56\22\4\16\0\3\4\3\0"+
    "\1\4\6\0\12\4\1\57\11\4\16\0\3\4\3\0"+
    "\1\4\6\0\1\4\1\60\5\4\1\61\10\4\1\62"+
    "\3\4\16\0\3\4\3\0\1\4\6\0\2\4\1\63"+
    "\21\4\16\0\3\4\3\0\1\4\6\0\1\4\1\64"+
    "\11\4\1\65\6\4\1\66\1\4\16\0\3\4\3\0"+
    "\1\4\6\0\2\4\1\67\1\4\1\70\17\4\16\0"+
    "\3\4\3\0\1\4\6\0\7\4\1\71\3\4\1\72"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\2\4\1\73"+
    "\1\4\1\74\11\4\1\75\5\4\16\0\3\4\3\0"+
    "\1\4\6\0\1\4\1\76\1\77\6\4\1\100\12\4"+
    "\16\0\1\4\1\101\1\4\3\0\1\4\6\0\13\4"+
    "\1\102\1\103\7\4\16\0\3\4\3\0\1\4\6\0"+
    "\2\4\1\104\21\4\16\0\3\4\3\0\1\4\6\0"+
    "\16\4\1\105\4\4\1\106\16\0\3\4\3\0\1\4"+
    "\6\0\13\4\1\107\10\4\16\0\3\4\3\0\1\4"+
    "\6\0\1\4\1\110\7\4\1\111\12\4\16\0\3\4"+
    "\3\0\1\4\6\0\2\4\1\112\21\4\16\0\3\4"+
    "\3\0\1\4\6\0\2\4\1\113\1\4\1\114\4\4"+
    "\1\115\12\4\16\0\3\4\3\0\1\4\6\0\2\4"+
    "\1\116\7\4\1\117\11\4\16\0\3\4\124\0\1\120"+
    "\56\0\1\121\3\0\1\122\52\0\1\123\14\0\1\4"+
    "\6\0\4\4\1\124\17\4\16\0\3\4\1\0\1\125"+
    "\64\0\1\126\52\0\1\4\6\0\2\4\1\127\7\4"+
    "\1\130\11\4\16\0\3\4\3\0\1\4\6\0\1\131"+
    "\3\4\1\132\14\4\1\133\2\4\16\0\3\4\3\0"+
    "\1\4\6\0\20\4\1\134\3\4\16\0\3\4\3\0"+
    "\1\4\6\0\20\4\1\135\3\4\16\0\3\4\3\0"+
    "\1\4\6\0\10\4\1\136\13\4\16\0\3\4\3\0"+
    "\1\4\6\0\1\4\1\137\22\4\16\0\3\4\3\0"+
    "\1\4\6\0\20\4\1\140\3\4\16\0\3\4\3\0"+
    "\1\4\6\0\15\4\1\141\6\4\16\0\3\4\3\0"+
    "\1\4\6\0\20\4\1\142\3\4\16\0\3\4\3\0"+
    "\1\4\6\0\24\4\16\0\1\143\2\4\3\0\1\4"+
    "\6\0\10\4\1\144\13\4\16\0\3\4\3\0\1\4"+
    "\6\0\1\4\1\145\22\4\16\0\3\4\3\0\1\4"+
    "\6\0\14\4\1\146\7\4\16\0\3\4\3\0\1\4"+
    "\6\0\13\4\1\147\10\4\16\0\3\4\3\0\1\4"+
    "\6\0\16\4\1\150\5\4\16\0\3\4\3\0\1\4"+
    "\6\0\12\4\1\151\11\4\16\0\3\4\3\0\1\4"+
    "\6\0\1\152\23\4\16\0\3\4\3\0\1\4\6\0"+
    "\20\4\1\153\3\4\16\0\3\4\3\0\1\4\6\0"+
    "\15\4\1\154\6\4\16\0\3\4\3\0\1\4\6\0"+
    "\10\4\1\155\13\4\16\0\3\4\3\0\1\4\6\0"+
    "\21\4\1\156\2\4\16\0\3\4\3\0\1\4\6\0"+
    "\1\4\1\157\22\4\16\0\3\4\3\0\1\4\6\0"+
    "\10\4\1\160\13\4\16\0\3\4\3\0\1\4\6\0"+
    "\6\4\1\161\15\4\16\0\3\4\3\0\1\4\6\0"+
    "\6\4\1\162\15\4\16\0\3\4\3\0\1\4\6\0"+
    "\17\4\1\163\4\4\16\0\3\4\3\0\1\4\6\0"+
    "\13\4\1\164\10\4\16\0\3\4\3\0\1\4\6\0"+
    "\15\4\1\165\6\4\16\0\3\4\3\0\1\4\6\0"+
    "\1\4\1\166\2\4\1\167\17\4\16\0\3\4\3\0"+
    "\1\4\6\0\2\4\1\170\21\4\16\0\3\4\3\0"+
    "\1\4\6\0\3\4\1\171\20\4\16\0\3\4\3\0"+
    "\1\4\6\0\1\4\1\172\22\4\16\0\3\4\3\0"+
    "\1\4\6\0\3\4\1\173\15\4\1\174\2\4\16\0"+
    "\3\4\3\0\1\4\6\0\20\4\1\175\3\4\16\0"+
    "\3\4\3\0\1\4\6\0\12\4\1\176\11\4\16\0"+
    "\3\4\3\0\1\4\6\0\14\4\1\177\3\4\1\200"+
    "\3\4\16\0\3\4\3\0\1\4\6\0\2\4\1\201"+
    "\21\4\16\0\3\4\3\0\1\4\6\0\2\4\1\202"+
    "\21\4\16\0\3\4\3\0\1\4\6\0\4\4\1\203"+
    "\17\4\16\0\3\4\3\0\1\4\6\0\6\4\1\204"+
    "\15\4\16\0\3\4\3\0\1\4\6\0\12\4\1\205"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\15\4\1\206"+
    "\6\4\16\0\3\4\3\0\1\4\6\0\21\4\1\207"+
    "\2\4\16\0\3\4\3\0\1\4\6\0\12\4\1\210"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\13\4\1\211"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\12\4\1\212"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\12\4\1\213"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\21\4\1\214"+
    "\2\4\16\0\3\4\3\0\1\4\6\0\10\4\1\215"+
    "\13\4\16\0\3\4\3\0\1\4\6\0\6\4\1\216"+
    "\15\4\16\0\3\4\3\0\1\4\6\0\10\4\1\217"+
    "\13\4\16\0\3\4\3\0\1\4\6\0\14\4\1\220"+
    "\7\4\16\0\3\4\3\0\1\4\6\0\13\4\1\221"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\15\4\1\222"+
    "\6\4\16\0\3\4\3\0\1\4\6\0\12\4\1\223"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\1\4\1\224"+
    "\22\4\16\0\3\4\3\0\1\4\6\0\14\4\1\225"+
    "\7\4\16\0\3\4\3\0\1\4\6\0\6\4\1\226"+
    "\15\4\16\0\3\4\3\0\1\4\6\0\1\4\1\227"+
    "\22\4\16\0\3\4\3\0\1\4\6\0\12\4\1\230"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\4\4\1\231"+
    "\17\4\16\0\3\4\3\0\1\4\6\0\1\4\1\232"+
    "\22\4\16\0\3\4\3\0\1\4\6\0\24\4\16\0"+
    "\1\4\1\233\1\4\3\0\1\4\6\0\13\4\1\234"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\3\4\1\235"+
    "\20\4\16\0\3\4\3\0\1\4\6\0\12\4\1\236"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\10\4\1\237"+
    "\13\4\16\0\3\4\3\0\1\4\6\0\14\4\1\240"+
    "\7\4\16\0\3\4\3\0\1\4\6\0\12\4\1\241"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\12\4\1\242"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\10\4\1\243"+
    "\13\4\16\0\3\4\3\0\1\4\6\0\10\4\1\244"+
    "\13\4\16\0\3\4\3\0\1\4\6\0\12\4\1\245"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\13\4\1\246"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\4\4\1\247"+
    "\17\4\16\0\3\4\3\0\1\4\6\0\20\4\1\250"+
    "\3\4\16\0\3\4\3\0\1\4\6\0\10\4\1\251"+
    "\13\4\16\0\3\4\3\0\1\4\6\0\20\4\1\252"+
    "\3\4\16\0\3\4\3\0\1\4\6\0\10\4\1\253"+
    "\13\4\16\0\3\4\3\0\1\4\6\0\12\4\1\254"+
    "\11\4\16\0\3\4\3\0\1\4\6\0\6\4\1\255"+
    "\15\4\16\0\3\4\3\0\1\4\6\0\14\4\1\256"+
    "\7\4\16\0\3\4\3\0\1\4\6\0\2\4\1\257"+
    "\21\4\16\0\3\4\3\0\1\4\6\0\4\4\1\260"+
    "\17\4\16\0\3\4\3\0\1\4\6\0\5\4\1\261"+
    "\16\4\16\0\3\4\3\0\1\4\6\0\16\4\1\262"+
    "\5\4\16\0\3\4\3\0\1\4\6\0\1\4\1\263"+
    "\22\4\16\0\3\4\3\0\1\4\6\0\2\4\1\264"+
    "\21\4\16\0\3\4\3\0\1\4\6\0\13\4\1\265"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\13\4\1\266"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\1\4\1\267"+
    "\22\4\16\0\3\4\3\0\1\4\6\0\13\4\1\270"+
    "\10\4\16\0\3\4\3\0\1\4\6\0\12\4\1\271"+
    "\11\4\16\0\3\4";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5922];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\6\1\2\0\21\1\6\11\1\1\3\11\2\1"+
    "\2\11\1\1\1\0\1\11\1\0\43\1\4\11\2\1"+
    "\1\11\143\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[185];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
    private Parser yyparser;
    public scanner (java.io.Reader r, Parser yyparser) {
      this(r);
      this.yyparser = yyparser;
    }
    public int getLine() {  
        return yyline;  
    } 
    public String getText() {  
        return yytext();  
    }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  scanner(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 158) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public int yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          { return 0; }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));return Parser.TOKEN_INT;
            }
          case 73: break;
          case 2: 
            { return Parser.TOKEN_DOT;
            }
          case 74: break;
          case 3: 
            { yyparser.yylval = new ParserVal(yytext());System.out.println(yytext());return Parser.TOKEN_ID;
            }
          case 75: break;
          case 4: 
            {
            }
          case 76: break;
          case 5: 
            { 
            }
          case 77: break;
          case 6: 
            { return Parser.TOKEN_LB;
            }
          case 78: break;
          case 7: 
            { return Parser.TOKEN_RB;
            }
          case 79: break;
          case 8: 
            { return Parser.TOKEN_SEMI;
            }
          case 80: break;
          case 9: 
            { return Parser.TOKEN_LP;
            }
          case 81: break;
          case 10: 
            { return Parser.TOKEN_RP;
            }
          case 82: break;
          case 11: 
            { return Parser.TOKEN_COMMA;
            }
          case 83: break;
          case 12: 
            { return Parser.TOKEN_COLON;
            }
          case 84: break;
          case 13: 
            { return Parser.TOKEN_EQUAL;
            }
          case 85: break;
          case 14: 
            { return Parser.TOKEN_PLUS;
            }
          case 86: break;
          case 15: 
            { return Parser.TOKEN_MINUS;
            }
          case 87: break;
          case 16: 
            { return Parser.TOKEN_LT;
            }
          case 88: break;
          case 17: 
            { return Parser.TOKEN_GT;
            }
          case 89: break;
          case 18: 
            { return Parser.TOKEN_MUL;
            }
          case 90: break;
          case 19: 
            { return Parser.TOKEN_DIV;
            }
          case 91: break;
          case 20: 
            { return Parser.TOKEN_DOTDOT;
            }
          case 92: break;
          case 21: 
            { yyparser.yylval = new ParserVal(yytext());return Parser.TOKEN_STRING;
            }
          case 93: break;
          case 22: 
            { return Parser.TOKEN_OR;
            }
          case 94: break;
          case 23: 
            { return Parser.TOKEN_OF;
            }
          case 95: break;
          case 24: 
            { return Parser.TOKEN_IF;
            }
          case 96: break;
          case 25: 
            { return Parser.TOKEN_TO;
            }
          case 97: break;
          case 26: 
            { return Parser.TOKEN_DO;
            }
          case 98: break;
          case 27: 
            { return Parser.TOKEN_ASSIGN;
            }
          case 99: break;
          case 28: 
            { return Parser.TOKEN_LE;
            }
          case 100: break;
          case 29: 
            { return Parser.TOKEN_UNEQUAL;
            }
          case 101: break;
          case 30: 
            { return Parser.TOKEN_GE;
            }
          case 102: break;
          case 31: 
            { yyparser.yylval = new ParserVal(Double.parseDouble(yytext()));return Parser.TOKEN_REAL;
            }
          case 103: break;
          case 32: 
            { yyparser.yylval = new ParserVal(yytext());return Parser.TOKEN_CHAR;
            }
          case 104: break;
          case 33: 
            { return Parser.TOKEN_ORD;
            }
          case 105: break;
          case 34: 
            { return Parser.TOKEN_ODD;
            }
          case 106: break;
          case 35: 
            { return Parser.TOKEN_AND;
            }
          case 107: break;
          case 36: 
            { return Parser.TOKEN_ABS;
            }
          case 108: break;
          case 37: 
            { return Parser.TOKEN_MOD;
            }
          case 109: break;
          case 38: 
            { return Parser.TOKEN_FOR;
            }
          case 110: break;
          case 39: 
            { return Parser.TOKEN_END;
            }
          case 111: break;
          case 40: 
            { return Parser.TOKEN_NOT;
            }
          case 112: break;
          case 41: 
            { return Parser.TOKEN_SQR;
            }
          case 113: break;
          case 42: 
            { return Parser.TOKEN_CHR;
            }
          case 114: break;
          case 43: 
            { return Parser.TOKEN_VAR;
            }
          case 115: break;
          case 44: 
            { return Parser.TOKEN_PRED;
            }
          case 116: break;
          case 45: 
            { return Parser.TOKEN_REAL_TYPE;
            }
          case 117: break;
          case 46: 
            { return Parser.TOKEN_READ;
            }
          case 118: break;
          case 47: 
            { return Parser.TOKEN_GOTO;
            }
          case 119: break;
          case 48: 
            { return Parser.TOKEN_TRUE;
            }
          case 120: break;
          case 49: 
            { return Parser.TOKEN_THEN;
            }
          case 121: break;
          case 50: 
            { return Parser.TOKEN_TYPE;
            }
          case 122: break;
          case 51: 
            { return Parser.TOKEN_ELSE;
            }
          case 123: break;
          case 52: 
            { return Parser.TOKEN_SUCC;
            }
          case 124: break;
          case 53: 
            { return Parser.TOKEN_SQRT;
            }
          case 125: break;
          case 54: 
            { return Parser.TOKEN_CASE;
            }
          case 126: break;
          case 55: 
            { return Parser.TOKEN_CHAR_TYPE;
            }
          case 127: break;
          case 56: 
            { return Parser.TOKEN_ARRAY;
            }
          case 128: break;
          case 57: 
            { return Parser.TOKEN_FALSE;
            }
          case 129: break;
          case 58: 
            { return Parser.TOKEN_UNTIL;
            }
          case 130: break;
          case 59: 
            { return Parser.TOKEN_WRITE;
            }
          case 131: break;
          case 60: 
            { return Parser.TOKEN_WHILE;
            }
          case 132: break;
          case 61: 
            { return Parser.TOKEN_CONST;
            }
          case 133: break;
          case 62: 
            { return Parser.TOKEN_BEGIN;
            }
          case 134: break;
          case 63: 
            { return Parser.TOKEN_REPEAT;
            }
          case 135: break;
          case 64: 
            { return Parser.TOKEN_RECORD;
            }
          case 136: break;
          case 65: 
            { return Parser.TOKEN_MAXINT;
            }
          case 137: break;
          case 66: 
            { return Parser.TOKEN_DOWNTO;
            }
          case 138: break;
          case 67: 
            { return Parser.TOKEN_PROGRAM;
            }
          case 139: break;
          case 68: 
            { return Parser.TOKEN_INTEGER_TYPE;
            }
          case 140: break;
          case 69: 
            { return Parser.TOKEN_WRITELN;
            }
          case 141: break;
          case 70: 
            { return Parser.TOKEN_BOOLEAN_TYPE;
            }
          case 142: break;
          case 71: 
            { return Parser.TOKEN_FUNCTION;
            }
          case 143: break;
          case 72: 
            { return Parser.TOKEN_PROCEDURE;
            }
          case 144: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
