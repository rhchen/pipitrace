// $ANTLR !Unknown version! Grammar/CTFParser.g 2012-03-09 14:59:18

package org.eclipse.linuxtools.ctf.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteEarlyExitException;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;

/**
 * TSDL parser. For internal use only.
 *
 * @author Matthew Khouzam and Antlr
 *
 */
@SuppressWarnings({ "nls", "unused", "unchecked", "rawtypes", "javadoc",
        "null", "incomplete-switch" })
public class TSDLParser extends Parser {
    public static final String[] tokenNames = new String[] { "<invalid>",
            "<EOR>", "<DOWN>", "<UP>", "ALIGNTOK", "CONSTTOK", "CHARTOK",
            "DOUBLETOK", "ENUMTOK", "EVENTTOK", "FLOATINGPOINTTOK", "FLOATTOK",
            "INTEGERTOK", "INTTOK", "LONGTOK", "SHORTTOK", "SIGNEDTOK",
            "STREAMTOK", "STRINGTOK", "STRUCTTOK", "TRACETOK", "TYPEALIASTOK",
            "TYPEDEFTOK", "UNSIGNEDTOK", "VARIANTTOK", "VOIDTOK", "BOOLTOK",
            "COMPLEXTOK", "IMAGINARYTOK", "ENVTOK", "CLOCKTOK", "NANNUMBERTOK",
            "INFINITYTOK", "NINFINITYTOK", "SEPARATOR", "COLON", "ELIPSES",
            "ASSIGNMENT", "TYPE_ASSIGNMENT", "LT", "GT", "OPENBRAC",
            "CLOSEBRAC", "LPAREN", "RPAREN", "LCURL", "RCURL", "TERM",
            "POINTER", "SIGN", "ARROW", "DOT", "BACKSLASH",
            "INTEGER_TYPES_SUFFIX", "OCTAL_LITERAL", "DIGIT",
            "DECIMAL_LITERAL", "HEX_PREFIX", "HEX_DIGIT", "HEX_LITERAL",
            "NONZERO_DIGIT", "OCTAL_ESCAPE", "UNICODE_ESCAPE",
            "HEXADECIMAL_ESCAPE", "ESCAPE_SEQUENCE", "STRINGPREFIX",
            "SINGLEQUOTE", "CHAR_CONTENT", "CHARACTER_LITERAL", "DOUBLEQUOTE",
            "STRING_CONTENT", "STRING_LITERAL", "WS", "COMMENT_OPEN",
            "COMMENT_CLOSE", "COMMENT", "LINE_COMMENT", "NONDIGIT",
            "IDENTIFIER", "ROOT", "EVENT", "STREAM", "TRACE", "ENV", "CLOCK",
            "DECLARATION", "SV_DECLARATION", "TYPE_SPECIFIER_LIST",
            "TYPE_DECLARATOR_LIST", "TYPE_DECLARATOR", "STRUCT", "STRUCT_NAME",
            "STRUCT_BODY", "ALIGN", "CTF_EXPRESSION_TYPE",
            "CTF_EXPRESSION_VAL", "CTF_LEFT", "CTF_RIGHT",
            "UNARY_EXPRESSION_STRING", "UNARY_EXPRESSION_STRING_QUOTES",
            "UNARY_EXPRESSION_DEC", "UNARY_EXPRESSION_HEX",
            "UNARY_EXPRESSION_OCT", "LENGTH", "TYPEDEF", "TYPEALIAS",
            "TYPEALIAS_TARGET", "TYPEALIAS_ALIAS", "INTEGER", "STRING",
            "FLOATING_POINT", "ENUM", "ENUM_CONTAINER_TYPE", "ENUM_ENUMERATOR",
            "ENUM_NAME", "ENUM_VALUE", "ENUM_VALUE_RANGE", "ENUM_BODY",
            "VARIANT", "VARIANT_NAME", "VARIANT_TAG", "VARIANT_BODY",
            "DECLARATOR" };
    public static final int SIGN = 49;
    public static final int LT = 39;
    public static final int TYPEDEFTOK = 22;
    public static final int VARIANT_NAME = 119;
    public static final int ENV = 83;
    public static final int INTEGER_TYPES_SUFFIX = 53;
    public static final int POINTER = 48;
    public static final int TRACE = 82;
    public static final int HEX_PREFIX = 57;
    public static final int INTTOK = 13;
    public static final int SEPARATOR = 34;
    public static final int ENUMTOK = 8;
    public static final int COMPLEXTOK = 27;
    public static final int IMAGINARYTOK = 28;
    public static final int STREAMTOK = 17;
    public static final int EOF = -1;
    public static final int UNARY_EXPRESSION_OCT = 102;
    public static final int ENUM_VALUE = 115;
    public static final int UNSIGNEDTOK = 23;
    public static final int ENUM_NAME = 114;
    public static final int RPAREN = 44;
    public static final int CHAR_CONTENT = 67;
    public static final int STRING_LITERAL = 71;
    public static final int UNARY_EXPRESSION_STRING_QUOTES = 99;
    public static final int ALIGNTOK = 4;
    public static final int FLOATTOK = 11;
    public static final int STRUCT_BODY = 92;
    public static final int ENUM_BODY = 117;
    public static final int COMMENT_CLOSE = 74;
    public static final int STRINGTOK = 18;
    public static final int COMMENT = 75;
    public static final int STREAM = 81;
    public static final int UNARY_EXPRESSION_HEX = 101;
    public static final int UNARY_EXPRESSION_DEC = 100;
    public static final int FLOATINGPOINTTOK = 10;
    public static final int LINE_COMMENT = 76;
    public static final int CTF_EXPRESSION_TYPE = 94;
    public static final int DOUBLETOK = 7;
    public static final int TYPE_DECLARATOR = 89;
    public static final int CHARACTER_LITERAL = 68;
    public static final int OCTAL_ESCAPE = 61;
    public static final int STRUCT_NAME = 91;
    public static final int VARIANT = 118;
    public static final int NANNUMBERTOK = 31;
    public static final int ENUM_ENUMERATOR = 113;
    public static final int FLOATING_POINT = 110;
    public static final int DECLARATOR = 122;
    public static final int SIGNEDTOK = 16;
    public static final int CHARTOK = 6;
    public static final int WS = 72;
    public static final int INTEGERTOK = 12;
    public static final int VARIANT_BODY = 121;
    public static final int NONDIGIT = 77;
    public static final int GT = 40;
    public static final int TYPEALIAS_TARGET = 106;
    public static final int DECIMAL_LITERAL = 56;
    public static final int BACKSLASH = 52;
    public static final int CLOSEBRAC = 42;
    public static final int TERM = 47;
    public static final int BOOLTOK = 26;
    public static final int CTF_RIGHT = 97;
    public static final int TYPE_DECLARATOR_LIST = 88;
    public static final int STRING_CONTENT = 70;
    public static final int TYPE_ASSIGNMENT = 38;
    public static final int ENUM_CONTAINER_TYPE = 112;
    public static final int DOUBLEQUOTE = 69;
    public static final int ENUM_VALUE_RANGE = 116;
    public static final int DECLARATION = 85;
    public static final int LENGTH = 103;
    public static final int LPAREN = 43;
    public static final int INFINITYTOK = 32;
    public static final int STRINGPREFIX = 65;
    public static final int CTF_EXPRESSION_VAL = 95;
    public static final int ESCAPE_SEQUENCE = 64;
    public static final int UNICODE_ESCAPE = 62;
    public static final int SINGLEQUOTE = 66;
    public static final int IDENTIFIER = 78;
    public static final int HEX_LITERAL = 59;
    public static final int ALIGN = 93;
    public static final int DIGIT = 55;
    public static final int DOT = 51;
    public static final int ENVTOK = 29;
    public static final int STRUCTTOK = 19;
    public static final int OPENBRAC = 41;
    public static final int TYPEALIASTOK = 21;
    public static final int CLOCK = 84;
    public static final int INTEGER = 108;
    public static final int TYPEALIAS = 105;
    public static final int EVENTTOK = 9;
    public static final int NINFINITYTOK = 33;
    public static final int TYPEDEF = 104;
    public static final int VOIDTOK = 25;
    public static final int TYPE_SPECIFIER_LIST = 87;
    public static final int OCTAL_LITERAL = 54;
    public static final int COMMENT_OPEN = 73;
    public static final int HEX_DIGIT = 58;
    public static final int STRUCT = 90;
    public static final int EVENT = 80;
    public static final int LONGTOK = 14;
    public static final int ROOT = 79;
    public static final int CTF_LEFT = 96;
    public static final int CLOCKTOK = 30;
    public static final int TRACETOK = 20;
    public static final int COLON = 35;
    public static final int HEXADECIMAL_ESCAPE = 63;
    public static final int LCURL = 45;
    public static final int VARIANTTOK = 24;
    public static final int VARIANT_TAG = 120;
    public static final int ENUM = 111;
    public static final int ELIPSES = 36;
    public static final int RCURL = 46;
    public static final int TYPEALIAS_ALIAS = 107;
    public static final int UNARY_EXPRESSION_STRING = 98;
    public static final int ARROW = 50;
    public static final int ASSIGNMENT = 37;
    public static final int SHORTTOK = 15;
    public static final int SV_DECLARATION = 86;
    public static final int NONZERO_DIGIT = 60;
    public static final int CONSTTOK = 5;
    public static final int STRING = 109;

    // delegates
    // delegators

    protected static class Symbols_scope {
        Set<String> types;
    }

    protected Stack Symbols_stack = new Stack();

    public TSDLParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public TSDLParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);

    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    @Override
    public String[] getTokenNames() {
        return TSDLParser.tokenNames;
    }

    @Override
    public String getGrammarFileName() {
        return "Grammar/CTFParser.g";
    }

    public TSDLParser(TokenStream input, boolean verbose) {
        this(input);
        this.verbose = verbose;
    }

    /*
     * To disable automatic error recovery. When we have a mismatched token,
     * simply throw an exception.
     */
    @Override
    protected Object recoverFromMismatchedToken(IntStream input, int ttype,
            BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

    /**
     * Checks if a given name has been defined has a type. From:
     * http://www.antlr.org/grammar/1153358328744/C.g
     *
     * @param name
     *            The name to check.
     * @return True if is is a type, false otherwise.
     */
    boolean isTypeName(String name) {
        for (int i = Symbols_stack.size() - 1; i >= 0; i--) {
            Symbols_scope scope = (Symbols_scope) Symbols_stack.get(i);
            if (scope.types.contains(name)) {
                return true;
            }
        }
        return false;
    }

    void addTypeName(String name) {
        ((Symbols_scope) Symbols_stack.peek()).types.add(name);
        if (verbose) {
            debug_print("New type: " + name);
        }
    }

    boolean _inTypedef = false;

    void typedefOn() {
        debug_print("typedefOn");
        _inTypedef = true;
    }

    void typedefOff() {
        debug_print("typedefOff");
        _inTypedef = false;
    }

    boolean inTypedef() {
        return _inTypedef;
    }

    boolean _inTypealiasAlias = false;

    void typealiasAliasOn() {
        debug_print("typealiasAliasOn");
        _inTypealiasAlias = true;
    }

    void typealiasAliasOff() {
        debug_print("typealiasAliasOff");
        _inTypealiasAlias = false;
    }

    boolean inTypealiasAlias() {
        return _inTypealiasAlias;
    }

    static void print_tabs(int n) {
        for (int i = 0; i < n; i++) {
            if(System.out != null) {//findbugs
                System.out.print("  ");
            }
        }
    }

    void enter(String name) {
        if (verbose) {
            if (state.backtracking == 0) {
                print_tabs(depth);
                debug_print("+ " + name);
                depth++;
            }
        }
    }

    void exit(String name) {
        if (verbose) {
            depth--;
            print_tabs(depth);
            debug_print("- " + name);
        }
    }

    void debug_print(String str) {
        if (verbose) {
            if(System.out != null) {//findbugs
                System.out.println(str);
            }
        }
    }

    int depth = 0;

    /* Prints rule entry and exit while parsing */
    boolean verbose = false;

    public static class parse_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "parse"
    // Grammar/CTFParser.g:198:1: parse : ( declaration )+ EOF -> ^( ROOT (
    // declaration )+ ) ;
    public final TSDLParser.parse_return parse() throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        TSDLParser.parse_return retval = new TSDLParser.parse_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF2 = null;
        TSDLParser.declaration_return declaration1 = null;

        CommonTree EOF2_tree = null;
        RewriteRuleTokenStream stream_EOF = new RewriteRuleTokenStream(adaptor,
                "token EOF");
        RewriteRuleSubtreeStream stream_declaration = new RewriteRuleSubtreeStream(
                adaptor, "rule declaration");

        enter("parse");
        debug_print("Scope push " + Symbols_stack.size());
        ((Symbols_scope) Symbols_stack.peek()).types = new HashSet<String>();

        try {
            // Grammar/CTFParser.g:211:1: ( ( declaration )+ EOF -> ^( ROOT (
            // declaration )+ ) )
            // Grammar/CTFParser.g:212:3: ( declaration )+ EOF
            {
                // Grammar/CTFParser.g:212:3: ( declaration )+
                int cnt1 = 0;
                loop1: do {
                    int alt1 = 2;
                    int LA1_0 = input.LA(1);

                    if ((((LA1_0 >= CONSTTOK) && (LA1_0 <= ENUMTOK))
                            || ((LA1_0 >= FLOATINGPOINTTOK) && (LA1_0 <= SIGNEDTOK))
                            || ((LA1_0 >= STRINGTOK) && (LA1_0 <= STRUCTTOK)) || ((LA1_0 >= TYPEDEFTOK) && (LA1_0 <= IMAGINARYTOK)))) {
                        alt1 = 1;
                    } else if ((LA1_0 == IDENTIFIER)
                            && ((inTypealiasAlias() || isTypeName(input.LT(1)
                                    .getText())))) {
                        alt1 = 1;
                    } else if (((LA1_0 == EVENTTOK) || (LA1_0 == STREAMTOK)
                            || ((LA1_0 >= TRACETOK) && (LA1_0 <= TYPEALIASTOK)) || ((LA1_0 >= ENVTOK) && (LA1_0 <= CLOCKTOK)))) {
                        alt1 = 1;
                    }

                    switch (alt1) {
                    case 1:
                    // Grammar/CTFParser.g:212:3: declaration
                    {
                        pushFollow(FOLLOW_declaration_in_parse321);
                        declaration1 = declaration();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_declaration.add(declaration1.getTree());
                        }

                    }
                        break;

                    default:
                        if (cnt1 >= 1) {
                            break loop1;
                        }
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        EarlyExitException eee = new EarlyExitException(1,
                                input);
                        throw eee;
                    }
                    cnt1++;
                } while (true);

                EOF2 = (Token) match(input, EOF, FOLLOW_EOF_in_parse324);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_EOF.add(EOF2);
                }

                // AST REWRITE
                // elements: declaration
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 212:20: -> ^( ROOT ( declaration )+ )
                    {
                        // Grammar/CTFParser.g:212:23: ^( ROOT ( declaration )+
                        // )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(ROOT, "ROOT"), root_1);

                            if (!(stream_declaration.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_declaration.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_declaration.nextTree());

                            }
                            stream_declaration.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print("Scope pop " + Symbols_stack.size());
                exit("parse");

                debug_print("Final depth, should be 0: " + depth);

            }
        }

        catch (RecognitionException e) {
            throw e;
        } finally {
            Symbols_stack.pop();

        }
        return retval;
    }

    // $ANTLR end "parse"

    public static class numberLiteral_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "numberLiteral"
    // Grammar/CTFParser.g:215:1: numberLiteral : ( SIGN )* ( HEX_LITERAL -> ^(
    // UNARY_EXPRESSION_HEX HEX_LITERAL ( SIGN )* ) | DECIMAL_LITERAL -> ^(
    // UNARY_EXPRESSION_DEC DECIMAL_LITERAL ( SIGN )* ) | OCTAL_LITERAL -> ^(
    // UNARY_EXPRESSION_OCT OCTAL_LITERAL ( SIGN )* ) ) ;
    public final TSDLParser.numberLiteral_return numberLiteral()
            throws RecognitionException {
        TSDLParser.numberLiteral_return retval = new TSDLParser.numberLiteral_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SIGN3 = null;
        Token HEX_LITERAL4 = null;
        Token DECIMAL_LITERAL5 = null;
        Token OCTAL_LITERAL6 = null;

        CommonTree SIGN3_tree = null;
        CommonTree HEX_LITERAL4_tree = null;
        CommonTree DECIMAL_LITERAL5_tree = null;
        CommonTree OCTAL_LITERAL6_tree = null;
        RewriteRuleTokenStream stream_SIGN = new RewriteRuleTokenStream(
                adaptor, "token SIGN");
        RewriteRuleTokenStream stream_OCTAL_LITERAL = new RewriteRuleTokenStream(
                adaptor, "token OCTAL_LITERAL");
        RewriteRuleTokenStream stream_HEX_LITERAL = new RewriteRuleTokenStream(
                adaptor, "token HEX_LITERAL");
        RewriteRuleTokenStream stream_DECIMAL_LITERAL = new RewriteRuleTokenStream(
                adaptor, "token DECIMAL_LITERAL");

        enter("numberLiteral");

        try {
            // Grammar/CTFParser.g:223:1: ( ( SIGN )* ( HEX_LITERAL -> ^(
            // UNARY_EXPRESSION_HEX HEX_LITERAL ( SIGN )* ) | DECIMAL_LITERAL ->
            // ^( UNARY_EXPRESSION_DEC DECIMAL_LITERAL ( SIGN )* ) |
            // OCTAL_LITERAL -> ^( UNARY_EXPRESSION_OCT OCTAL_LITERAL ( SIGN )*
            // ) ) )
            // Grammar/CTFParser.g:224:3: ( SIGN )* ( HEX_LITERAL -> ^(
            // UNARY_EXPRESSION_HEX HEX_LITERAL ( SIGN )* ) | DECIMAL_LITERAL ->
            // ^( UNARY_EXPRESSION_DEC DECIMAL_LITERAL ( SIGN )* ) |
            // OCTAL_LITERAL -> ^( UNARY_EXPRESSION_OCT OCTAL_LITERAL ( SIGN )*
            // ) )
            {
                // Grammar/CTFParser.g:224:3: ( SIGN )*
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);

                    if ((LA2_0 == SIGN)) {
                        alt2 = 1;
                    }

                    switch (alt2) {
                    case 1:
                    // Grammar/CTFParser.g:224:3: SIGN
                    {
                        SIGN3 = (Token) match(input, SIGN,
                                FOLLOW_SIGN_in_numberLiteral357);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_SIGN.add(SIGN3);
                        }

                    }
                        break;

                    default:
                        break loop2;
                    }
                } while (true);

                // Grammar/CTFParser.g:224:10: ( HEX_LITERAL -> ^(
                // UNARY_EXPRESSION_HEX HEX_LITERAL ( SIGN )* ) |
                // DECIMAL_LITERAL -> ^( UNARY_EXPRESSION_DEC DECIMAL_LITERAL (
                // SIGN )* ) | OCTAL_LITERAL -> ^( UNARY_EXPRESSION_OCT
                // OCTAL_LITERAL ( SIGN )* ) )
                int alt3 = 3;
                switch (input.LA(1)) {
                case HEX_LITERAL: {
                    alt3 = 1;
                }
                    break;
                case DECIMAL_LITERAL: {
                    alt3 = 2;
                }
                    break;
                case OCTAL_LITERAL: {
                    alt3 = 3;
                }
                    break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("", 3,
                            0, input);

                    throw nvae;
                }

                switch (alt3) {
                case 1:
                // Grammar/CTFParser.g:224:11: HEX_LITERAL
                {
                    HEX_LITERAL4 = (Token) match(input, HEX_LITERAL,
                            FOLLOW_HEX_LITERAL_in_numberLiteral362);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_HEX_LITERAL.add(HEX_LITERAL4);
                    }

                    // AST REWRITE
                    // elements: SIGN, HEX_LITERAL
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if (state.backtracking == 0) {
                        retval.tree = root_0;
//                        RewriteRuleSubtreeStream stream_retval =
                                new RewriteRuleSubtreeStream(
                                adaptor, "rule retval",
                                retval != null ? retval.tree : null);

                        root_0 = (CommonTree) adaptor.nil();
                        // 224:23: -> ^( UNARY_EXPRESSION_HEX HEX_LITERAL ( SIGN
                        // )* )
                        {
                            // Grammar/CTFParser.g:224:26: ^(
                            // UNARY_EXPRESSION_HEX HEX_LITERAL ( SIGN )* )
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor
                                        .becomeRoot(adaptor.create(
                                                UNARY_EXPRESSION_HEX,
                                                "UNARY_EXPRESSION_HEX"), root_1);

                                adaptor.addChild(root_1,
                                        stream_HEX_LITERAL.nextNode());
                                // Grammar/CTFParser.g:224:61: ( SIGN )*
                                while (stream_SIGN.hasNext()) {
                                    adaptor.addChild(root_1,
                                            stream_SIGN.nextNode());

                                }
                                stream_SIGN.reset();

                                adaptor.addChild(root_0, root_1);
                            }

                        }

                        retval.tree = root_0;
                    }
                }
                    break;
                case 2:
                // Grammar/CTFParser.g:225:5: DECIMAL_LITERAL
                {
                    DECIMAL_LITERAL5 = (Token) match(input, DECIMAL_LITERAL,
                            FOLLOW_DECIMAL_LITERAL_in_numberLiteral379);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_DECIMAL_LITERAL.add(DECIMAL_LITERAL5);
                    }

                    // AST REWRITE
                    // elements: DECIMAL_LITERAL, SIGN
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if (state.backtracking == 0) {
                        retval.tree = root_0;
//                        RewriteRuleSubtreeStream stream_retval =
                                new RewriteRuleSubtreeStream(
                                adaptor, "rule retval",
                                retval != null ? retval.tree : null);

                        root_0 = (CommonTree) adaptor.nil();
                        // 225:21: -> ^( UNARY_EXPRESSION_DEC DECIMAL_LITERAL (
                        // SIGN )* )
                        {
                            // Grammar/CTFParser.g:225:24: ^(
                            // UNARY_EXPRESSION_DEC DECIMAL_LITERAL ( SIGN )* )
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor
                                        .becomeRoot(adaptor.create(
                                                UNARY_EXPRESSION_DEC,
                                                "UNARY_EXPRESSION_DEC"), root_1);

                                adaptor.addChild(root_1,
                                        stream_DECIMAL_LITERAL.nextNode());
                                // Grammar/CTFParser.g:225:63: ( SIGN )*
                                while (stream_SIGN.hasNext()) {
                                    adaptor.addChild(root_1,
                                            stream_SIGN.nextNode());

                                }
                                stream_SIGN.reset();

                                adaptor.addChild(root_0, root_1);
                            }

                        }

                        retval.tree = root_0;
                    }
                }
                    break;
                case 3:
                // Grammar/CTFParser.g:226:5: OCTAL_LITERAL
                {
                    OCTAL_LITERAL6 = (Token) match(input, OCTAL_LITERAL,
                            FOLLOW_OCTAL_LITERAL_in_numberLiteral396);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_OCTAL_LITERAL.add(OCTAL_LITERAL6);
                    }

                    // AST REWRITE
                    // elements: SIGN, OCTAL_LITERAL
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if (state.backtracking == 0) {
                        retval.tree = root_0;
//                        RewriteRuleSubtreeStream stream_retval =
                                new RewriteRuleSubtreeStream(
                                adaptor, "rule retval",
                                retval != null ? retval.tree : null);

                        root_0 = (CommonTree) adaptor.nil();
                        // 226:19: -> ^( UNARY_EXPRESSION_OCT OCTAL_LITERAL (
                        // SIGN )* )
                        {
                            // Grammar/CTFParser.g:226:22: ^(
                            // UNARY_EXPRESSION_OCT OCTAL_LITERAL ( SIGN )* )
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor
                                        .becomeRoot(adaptor.create(
                                                UNARY_EXPRESSION_OCT,
                                                "UNARY_EXPRESSION_OCT"), root_1);

                                adaptor.addChild(root_1,
                                        stream_OCTAL_LITERAL.nextNode());
                                // Grammar/CTFParser.g:226:59: ( SIGN )*
                                while (stream_SIGN.hasNext()) {
                                    adaptor.addChild(root_1,
                                            stream_SIGN.nextNode());

                                }
                                stream_SIGN.reset();

                                adaptor.addChild(root_0, root_1);
                            }

                        }

                        retval.tree = root_0;
                    }
                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("numberLiteral");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "numberLiteral"

    public static class constant_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "constant"
    // Grammar/CTFParser.g:229:1: constant : ( numberLiteral | enumConstant |
    // CHARACTER_LITERAL );
    public final TSDLParser.constant_return constant()
            throws RecognitionException {
        TSDLParser.constant_return retval = new TSDLParser.constant_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CHARACTER_LITERAL9 = null;
        TSDLParser.numberLiteral_return numberLiteral7 = null;

        TSDLParser.enumConstant_return enumConstant8 = null;

        CommonTree CHARACTER_LITERAL9_tree = null;

        enter("constant");

        try {
            // Grammar/CTFParser.g:236:1: ( numberLiteral | enumConstant |
            // CHARACTER_LITERAL )
            int alt4 = 3;
            switch (input.LA(1)) {
            case SIGN:
            case OCTAL_LITERAL:
            case DECIMAL_LITERAL:
            case HEX_LITERAL: {
                alt4 = 1;
            }
                break;
            case ALIGNTOK:
            case EVENTTOK:
            case SIGNEDTOK:
            case STRINGTOK:
            case STRING_LITERAL:
            case IDENTIFIER: {
                alt4 = 2;
            }
                break;
            case CHARACTER_LITERAL: {
                alt4 = 3;
            }
                break;
            default:
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 4, 0,
                        input);

                throw nvae;
            }

            switch (alt4) {
            case 1:
            // Grammar/CTFParser.g:237:4: numberLiteral
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_numberLiteral_in_constant432);
                numberLiteral7 = numberLiteral();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, numberLiteral7.getTree());
                }

            }
                break;
            case 2:
            // Grammar/CTFParser.g:238:5: enumConstant
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_enumConstant_in_constant438);
                enumConstant8 = enumConstant();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, enumConstant8.getTree());
                }

            }
                break;
            case 3:
            // Grammar/CTFParser.g:239:5: CHARACTER_LITERAL
            {
                root_0 = (CommonTree) adaptor.nil();

                CHARACTER_LITERAL9 = (Token) match(input, CHARACTER_LITERAL,
                        FOLLOW_CHARACTER_LITERAL_in_constant444);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    CHARACTER_LITERAL9_tree = (CommonTree) adaptor
                            .create(CHARACTER_LITERAL9);
                    adaptor.addChild(root_0, CHARACTER_LITERAL9_tree);
                }

            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("constant");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "constant"

    public static class primaryExpression_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "primaryExpression"
    // Grammar/CTFParser.g:242:1: primaryExpression : ( ( IDENTIFIER )=>
    // IDENTIFIER -> ^( UNARY_EXPRESSION_STRING IDENTIFIER ) | ( ctfKeyword )=>
    // ctfKeyword -> ^( UNARY_EXPRESSION_STRING ctfKeyword ) | ( STRING_LITERAL
    // )=> STRING_LITERAL -> ^( UNARY_EXPRESSION_STRING_QUOTES STRING_LITERAL )
    // | constant );
    public final TSDLParser.primaryExpression_return primaryExpression()
            throws RecognitionException {
        TSDLParser.primaryExpression_return retval = new TSDLParser.primaryExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENTIFIER10 = null;
        Token STRING_LITERAL12 = null;
        TSDLParser.ctfKeyword_return ctfKeyword11 = null;

        TSDLParser.constant_return constant13 = null;

        CommonTree IDENTIFIER10_tree = null;
        CommonTree STRING_LITERAL12_tree = null;
        RewriteRuleTokenStream stream_STRING_LITERAL = new RewriteRuleTokenStream(
                adaptor, "token STRING_LITERAL");
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(
                adaptor, "token IDENTIFIER");
        RewriteRuleSubtreeStream stream_ctfKeyword = new RewriteRuleSubtreeStream(
                adaptor, "rule ctfKeyword");

        enter("primaryExpression");

        try {
            // Grammar/CTFParser.g:249:1: ( ( IDENTIFIER )=> IDENTIFIER -> ^(
            // UNARY_EXPRESSION_STRING IDENTIFIER ) | ( ctfKeyword )=>
            // ctfKeyword -> ^( UNARY_EXPRESSION_STRING ctfKeyword ) | (
            // STRING_LITERAL )=> STRING_LITERAL -> ^(
            // UNARY_EXPRESSION_STRING_QUOTES STRING_LITERAL ) | constant )
            int alt5 = 4;
            switch (input.LA(1)) {
            case IDENTIFIER: {
                int LA5_1 = input.LA(2);

                if ((synpred1_CTFParser())) {
                    alt5 = 1;
                } else if ((true)) {
                    alt5 = 4;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("", 5,
                            1, input);

                    throw nvae;
                }
            }
                break;
            case ALIGNTOK:
            case EVENTTOK:
            case SIGNEDTOK:
            case STRINGTOK: {
                int LA5_2 = input.LA(2);

                if ((synpred2_CTFParser())) {
                    alt5 = 2;
                } else if ((true)) {
                    alt5 = 4;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("", 5,
                            2, input);

                    throw nvae;
                }
            }
                break;
            case STRING_LITERAL: {
                int LA5_3 = input.LA(2);

                if ((synpred3_CTFParser())) {
                    alt5 = 3;
                } else if ((true)) {
                    alt5 = 4;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("", 5,
                            3, input);

                    throw nvae;
                }
            }
                break;
            case SIGN:
            case OCTAL_LITERAL:
            case DECIMAL_LITERAL:
            case HEX_LITERAL:
            case CHARACTER_LITERAL: {
                alt5 = 4;
            }
                break;
            default:
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 5, 0,
                        input);

                throw nvae;
            }

            switch (alt5) {
            case 1:
            // Grammar/CTFParser.g:250:5: ( IDENTIFIER )=> IDENTIFIER
            {
                IDENTIFIER10 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_primaryExpression475);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_IDENTIFIER.add(IDENTIFIER10);
                }

                if (state.backtracking == 0) {
                    debug_print("IDENTIFIER: "
                            + (IDENTIFIER10 != null ? IDENTIFIER10.getText()
                                    : null));
                }

                // AST REWRITE
                // elements: IDENTIFIER
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 250:83: -> ^( UNARY_EXPRESSION_STRING IDENTIFIER )
                    {
                        // Grammar/CTFParser.g:250:86: ^(
                        // UNARY_EXPRESSION_STRING IDENTIFIER )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(UNARY_EXPRESSION_STRING,
                                            "UNARY_EXPRESSION_STRING"), root_1);

                            adaptor.addChild(root_1,
                                    stream_IDENTIFIER.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 2:
            // Grammar/CTFParser.g:251:5: ( ctfKeyword )=> ctfKeyword
            {
                pushFollow(FOLLOW_ctfKeyword_in_primaryExpression497);
                ctfKeyword11 = ctfKeyword();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ctfKeyword.add(ctfKeyword11.getTree());
                }

                // AST REWRITE
                // elements: ctfKeyword
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 251:32: -> ^( UNARY_EXPRESSION_STRING ctfKeyword )
                    {
                        // Grammar/CTFParser.g:251:35: ^(
                        // UNARY_EXPRESSION_STRING ctfKeyword )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(UNARY_EXPRESSION_STRING,
                                            "UNARY_EXPRESSION_STRING"), root_1);

                            adaptor.addChild(root_1,
                                    stream_ctfKeyword.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 3:
            // Grammar/CTFParser.g:252:5: ( STRING_LITERAL )=> STRING_LITERAL
            {
                STRING_LITERAL12 = (Token) match(input, STRING_LITERAL,
                        FOLLOW_STRING_LITERAL_in_primaryExpression517);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_STRING_LITERAL.add(STRING_LITERAL12);
                }

                if (state.backtracking == 0) {
                    debug_print("STRING_LITERAL: "
                            + (STRING_LITERAL12 != null ? STRING_LITERAL12
                                    .getText() : null));
                }

                // AST REWRITE
                // elements: STRING_LITERAL
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 252:99: -> ^( UNARY_EXPRESSION_STRING_QUOTES
                    // STRING_LITERAL )
                    {
                        // Grammar/CTFParser.g:252:102: ^(
                        // UNARY_EXPRESSION_STRING_QUOTES STRING_LITERAL )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(UNARY_EXPRESSION_STRING_QUOTES,
                                            "UNARY_EXPRESSION_STRING_QUOTES"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_STRING_LITERAL.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 4:
            // Grammar/CTFParser.g:254:5: constant
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_constant_in_primaryExpression538);
                constant13 = constant();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, constant13.getTree());
                }

            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("primaryExpression");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "primaryExpression"

    public static class reference_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "reference"
    // Grammar/CTFParser.g:257:1: reference : (ref= DOT | ref= ARROW )
    // IDENTIFIER -> ^( $ref ^( UNARY_EXPRESSION_STRING IDENTIFIER ) ) ;
    public final TSDLParser.reference_return reference()
            throws RecognitionException {
        TSDLParser.reference_return retval = new TSDLParser.reference_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ref = null;
        Token IDENTIFIER14 = null;

        CommonTree ref_tree = null;
        CommonTree IDENTIFIER14_tree = null;
        RewriteRuleTokenStream stream_ARROW = new RewriteRuleTokenStream(
                adaptor, "token ARROW");
        RewriteRuleTokenStream stream_DOT = new RewriteRuleTokenStream(adaptor,
                "token DOT");
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(
                adaptor, "token IDENTIFIER");

        enter("reference");

        try {
            // Grammar/CTFParser.g:265:1: ( (ref= DOT | ref= ARROW ) IDENTIFIER
            // -> ^( $ref ^( UNARY_EXPRESSION_STRING IDENTIFIER ) ) )
            // Grammar/CTFParser.g:266:3: (ref= DOT | ref= ARROW ) IDENTIFIER
            {
                // Grammar/CTFParser.g:266:3: (ref= DOT | ref= ARROW )
                int alt6 = 2;
                int LA6_0 = input.LA(1);

                if ((LA6_0 == DOT)) {
                    alt6 = 1;
                } else if ((LA6_0 == ARROW)) {
                    alt6 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("", 6,
                            0, input);

                    throw nvae;
                }
                switch (alt6) {
                case 1:
                // Grammar/CTFParser.g:266:4: ref= DOT
                {
                    ref = (Token) match(input, DOT, FOLLOW_DOT_in_reference564);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_DOT.add(ref);
                    }

                }
                    break;
                case 2:
                // Grammar/CTFParser.g:266:14: ref= ARROW
                {
                    ref = (Token) match(input, ARROW,
                            FOLLOW_ARROW_in_reference570);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_ARROW.add(ref);
                    }

                }
                    break;

                }

                IDENTIFIER14 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_reference573);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_IDENTIFIER.add(IDENTIFIER14);
                }

                // AST REWRITE
                // elements: IDENTIFIER, ref
                // token labels: ref
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_ref = new RewriteRuleTokenStream(
                            adaptor, "token ref", ref);
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 266:36: -> ^( $ref ^( UNARY_EXPRESSION_STRING IDENTIFIER
                    // ) )
                    {
                        // Grammar/CTFParser.g:266:39: ^( $ref ^(
                        // UNARY_EXPRESSION_STRING IDENTIFIER ) )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    stream_ref.nextNode(), root_1);

                            // Grammar/CTFParser.g:266:46: ^(
                            // UNARY_EXPRESSION_STRING IDENTIFIER )
                            {
                                CommonTree root_2 = (CommonTree) adaptor.nil();
                                root_2 = (CommonTree) adaptor.becomeRoot(
                                        adaptor.create(UNARY_EXPRESSION_STRING,
                                                "UNARY_EXPRESSION_STRING"),
                                        root_2);

                                adaptor.addChild(root_2,
                                        stream_IDENTIFIER.nextNode());

                                adaptor.addChild(root_1, root_2);
                            }

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("reference");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "reference"

    public static class postfixExpressionSuffix_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "postfixExpressionSuffix"
    // Grammar/CTFParser.g:269:1: postfixExpressionSuffix : ( ( OPENBRAC
    // unaryExpression CLOSEBRAC ) | reference );
    public final TSDLParser.postfixExpressionSuffix_return postfixExpressionSuffix()
            throws RecognitionException {
        TSDLParser.postfixExpressionSuffix_return retval = new TSDLParser.postfixExpressionSuffix_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token OPENBRAC15 = null;
        Token CLOSEBRAC17 = null;
        TSDLParser.unaryExpression_return unaryExpression16 = null;

        TSDLParser.reference_return reference18 = null;

        CommonTree OPENBRAC15_tree = null;
        CommonTree CLOSEBRAC17_tree = null;

        enter("postfixExpressionSuffix");

        try {
            // Grammar/CTFParser.g:276:1: ( ( OPENBRAC unaryExpression CLOSEBRAC
            // ) | reference )
            int alt7 = 2;
            int LA7_0 = input.LA(1);

            if ((LA7_0 == OPENBRAC)) {
                alt7 = 1;
            } else if ((((LA7_0 >= ARROW) && (LA7_0 <= DOT)))) {
                alt7 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 7, 0,
                        input);

                throw nvae;
            }
            switch (alt7) {
            case 1:
            // Grammar/CTFParser.g:277:5: ( OPENBRAC unaryExpression CLOSEBRAC )
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:277:5: ( OPENBRAC unaryExpression
                // CLOSEBRAC )
                // Grammar/CTFParser.g:277:6: OPENBRAC unaryExpression CLOSEBRAC
                {
                    OPENBRAC15 = (Token) match(input, OPENBRAC,
                            FOLLOW_OPENBRAC_in_postfixExpressionSuffix612);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        OPENBRAC15_tree = (CommonTree) adaptor
                                .create(OPENBRAC15);
                        adaptor.addChild(root_0, OPENBRAC15_tree);
                    }
                    pushFollow(FOLLOW_unaryExpression_in_postfixExpressionSuffix614);
                    unaryExpression16 = unaryExpression();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        adaptor.addChild(root_0, unaryExpression16.getTree());
                    }
//                    CLOSEBRAC17 = (Token)
                            match(input, CLOSEBRAC,
                            FOLLOW_CLOSEBRAC_in_postfixExpressionSuffix616);
                    if (state.failed) {
                        return retval;
                    }

                }

            }
                break;
            case 2:
            // Grammar/CTFParser.g:278:5: reference
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_reference_in_postfixExpressionSuffix624);
                reference18 = reference();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, reference18.getTree());
                }

            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("postfixExpressionSuffix");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "postfixExpressionSuffix"

    public static class postfixExpression_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "postfixExpression"
    // Grammar/CTFParser.g:281:1: postfixExpression : ( ( primaryExpression ) (
    // postfixExpressionSuffix )* | ( ( ctfSpecifierHead ) (
    // postfixExpressionSuffix )+ ) );
    public final TSDLParser.postfixExpression_return postfixExpression()
            throws RecognitionException {
        TSDLParser.postfixExpression_return retval = new TSDLParser.postfixExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.primaryExpression_return primaryExpression19 = null;

        TSDLParser.postfixExpressionSuffix_return postfixExpressionSuffix20 = null;

        TSDLParser.ctfSpecifierHead_return ctfSpecifierHead21 = null;

        TSDLParser.postfixExpressionSuffix_return postfixExpressionSuffix22 = null;

        enter("postfixExpression");

        try {
            // Grammar/CTFParser.g:288:1: ( ( primaryExpression ) (
            // postfixExpressionSuffix )* | ( ( ctfSpecifierHead ) (
            // postfixExpressionSuffix )+ ) )
            int alt10 = 2;
            alt10 = dfa10.predict(input);
            switch (alt10) {
            case 1:
            // Grammar/CTFParser.g:289:3: ( primaryExpression ) (
            // postfixExpressionSuffix )*
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:289:3: ( primaryExpression )
                // Grammar/CTFParser.g:289:4: primaryExpression
                {
                    pushFollow(FOLLOW_primaryExpression_in_postfixExpression648);
                    primaryExpression19 = primaryExpression();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        adaptor.addChild(root_0, primaryExpression19.getTree());
                    }

                }

                // Grammar/CTFParser.g:289:23: ( postfixExpressionSuffix )*
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);

                    if (((LA8_0 == OPENBRAC) || ((LA8_0 >= ARROW) && (LA8_0 <= DOT)))) {
                        alt8 = 1;
                    }

                    switch (alt8) {
                    case 1:
                    // Grammar/CTFParser.g:289:24: postfixExpressionSuffix
                    {
                        pushFollow(FOLLOW_postfixExpressionSuffix_in_postfixExpression652);
                        postfixExpressionSuffix20 = postfixExpressionSuffix();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    postfixExpressionSuffix20.getTree());
                        }

                    }
                        break;

                    default:
                        break loop8;
                    }
                } while (true);

            }
                break;
            case 2:
            // Grammar/CTFParser.g:290:3: ( ( ctfSpecifierHead ) (
            // postfixExpressionSuffix )+ )
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:290:3: ( ( ctfSpecifierHead ) (
                // postfixExpressionSuffix )+ )
                // Grammar/CTFParser.g:290:4: ( ctfSpecifierHead ) (
                // postfixExpressionSuffix )+
                {
                    // Grammar/CTFParser.g:290:4: ( ctfSpecifierHead )
                    // Grammar/CTFParser.g:290:5: ctfSpecifierHead
                    {
                        pushFollow(FOLLOW_ctfSpecifierHead_in_postfixExpression661);
                        ctfSpecifierHead21 = ctfSpecifierHead();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    ctfSpecifierHead21.getTree());
                        }

                    }

                    // Grammar/CTFParser.g:290:24: ( postfixExpressionSuffix )+
                    int cnt9 = 0;
                    loop9: do {
                        int alt9 = 2;
                        int LA9_0 = input.LA(1);

                        if (((LA9_0 == OPENBRAC) || ((LA9_0 >= ARROW) && (LA9_0 <= DOT)))) {
                            alt9 = 1;
                        }

                        switch (alt9) {
                        case 1:
                        // Grammar/CTFParser.g:290:25: postfixExpressionSuffix
                        {
                            pushFollow(FOLLOW_postfixExpressionSuffix_in_postfixExpression666);
                            postfixExpressionSuffix22 = postfixExpressionSuffix();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                adaptor.addChild(root_0,
                                        postfixExpressionSuffix22.getTree());
                            }

                        }
                            break;

                        default:
                            if (cnt9 >= 1) {
                                break loop9;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return retval;
                            }
                            EarlyExitException eee = new EarlyExitException(9,
                                    input);
                            throw eee;
                        }
                        cnt9++;
                    } while (true);

                }

            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("postfixExpression");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "postfixExpression"

    public static class unaryExpression_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "unaryExpression"
    // Grammar/CTFParser.g:293:1: unaryExpression : postfixExpression ;
    public final TSDLParser.unaryExpression_return unaryExpression()
            throws RecognitionException {
        TSDLParser.unaryExpression_return retval = new TSDLParser.unaryExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.postfixExpression_return postfixExpression23 = null;

        enter("unaryExpression");

        try {
            // Grammar/CTFParser.g:300:1: ( postfixExpression )
            // Grammar/CTFParser.g:303:5: postfixExpression
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_postfixExpression_in_unaryExpression700);
                postfixExpression23 = postfixExpression();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, postfixExpression23.getTree());
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("unaryExpression");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "unaryExpression"

    public static class enumConstant_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumConstant"
    // Grammar/CTFParser.g:306:1: enumConstant : ( STRING_LITERAL -> ^(
    // UNARY_EXPRESSION_STRING_QUOTES STRING_LITERAL ) | IDENTIFIER -> ^(
    // UNARY_EXPRESSION_STRING IDENTIFIER ) | ctfKeyword -> ^(
    // UNARY_EXPRESSION_STRING ctfKeyword ) );
    public final TSDLParser.enumConstant_return enumConstant()
            throws RecognitionException {
        TSDLParser.enumConstant_return retval = new TSDLParser.enumConstant_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token STRING_LITERAL24 = null;
        Token IDENTIFIER25 = null;
        TSDLParser.ctfKeyword_return ctfKeyword26 = null;

        CommonTree STRING_LITERAL24_tree = null;
        CommonTree IDENTIFIER25_tree = null;
        RewriteRuleTokenStream stream_STRING_LITERAL = new RewriteRuleTokenStream(
                adaptor, "token STRING_LITERAL");
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(
                adaptor, "token IDENTIFIER");
        RewriteRuleSubtreeStream stream_ctfKeyword = new RewriteRuleSubtreeStream(
                adaptor, "rule ctfKeyword");

        enter("enumConstant");

        try {
            // Grammar/CTFParser.g:314:1: ( STRING_LITERAL -> ^(
            // UNARY_EXPRESSION_STRING_QUOTES STRING_LITERAL ) | IDENTIFIER ->
            // ^( UNARY_EXPRESSION_STRING IDENTIFIER ) | ctfKeyword -> ^(
            // UNARY_EXPRESSION_STRING ctfKeyword ) )
            int alt11 = 3;
            switch (input.LA(1)) {
            case STRING_LITERAL: {
                alt11 = 1;
            }
                break;
            case IDENTIFIER: {
                alt11 = 2;
            }
                break;
            case ALIGNTOK:
            case EVENTTOK:
            case SIGNEDTOK:
            case STRINGTOK: {
                alt11 = 3;
            }
                break;
            default:
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 11, 0,
                        input);

                throw nvae;
            }

            switch (alt11) {
            case 1:
            // Grammar/CTFParser.g:315:5: STRING_LITERAL
            {
                STRING_LITERAL24 = (Token) match(input, STRING_LITERAL,
                        FOLLOW_STRING_LITERAL_in_enumConstant725);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_STRING_LITERAL.add(STRING_LITERAL24);
                }

                // AST REWRITE
                // elements: STRING_LITERAL
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 315:20: -> ^( UNARY_EXPRESSION_STRING_QUOTES
                    // STRING_LITERAL )
                    {
                        // Grammar/CTFParser.g:315:23: ^(
                        // UNARY_EXPRESSION_STRING_QUOTES STRING_LITERAL )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(UNARY_EXPRESSION_STRING_QUOTES,
                                            "UNARY_EXPRESSION_STRING_QUOTES"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_STRING_LITERAL.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 2:
            // Grammar/CTFParser.g:316:7: IDENTIFIER
            {
                IDENTIFIER25 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_enumConstant741);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_IDENTIFIER.add(IDENTIFIER25);
                }

                // AST REWRITE
                // elements: IDENTIFIER
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 316:18: -> ^( UNARY_EXPRESSION_STRING IDENTIFIER )
                    {
                        // Grammar/CTFParser.g:316:21: ^(
                        // UNARY_EXPRESSION_STRING IDENTIFIER )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(UNARY_EXPRESSION_STRING,
                                            "UNARY_EXPRESSION_STRING"), root_1);

                            adaptor.addChild(root_1,
                                    stream_IDENTIFIER.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 3:
            // Grammar/CTFParser.g:317:7: ctfKeyword
            {
                pushFollow(FOLLOW_ctfKeyword_in_enumConstant757);
                ctfKeyword26 = ctfKeyword();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ctfKeyword.add(ctfKeyword26.getTree());
                }

                // AST REWRITE
                // elements: ctfKeyword
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 317:18: -> ^( UNARY_EXPRESSION_STRING ctfKeyword )
                    {
                        // Grammar/CTFParser.g:317:21: ^(
                        // UNARY_EXPRESSION_STRING ctfKeyword )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(UNARY_EXPRESSION_STRING,
                                            "UNARY_EXPRESSION_STRING"), root_1);

                            adaptor.addChild(root_1,
                                    stream_ctfKeyword.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("enumConstant");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "enumConstant"

    public static class declaration_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "declaration"
    // Grammar/CTFParser.g:321:1: declaration : ( ( declarationSpecifiers (
    // declaratorList )? TERM ) -> {inTypedef()}? ^( DECLARATION ^( TYPEDEF
    // declaratorList declarationSpecifiers ) ) -> ^( DECLARATION
    // declarationSpecifiers ( declaratorList )? ) | ( ctfSpecifier TERM ) );
    public final TSDLParser.declaration_return declaration()
            throws RecognitionException {
        TSDLParser.declaration_return retval = new TSDLParser.declaration_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token TERM29 = null;
        Token TERM31 = null;
        TSDLParser.declarationSpecifiers_return declarationSpecifiers27 = null;

        TSDLParser.declaratorList_return declaratorList28 = null;

        TSDLParser.ctfSpecifier_return ctfSpecifier30 = null;

        CommonTree TERM29_tree = null;
        CommonTree TERM31_tree = null;
        RewriteRuleTokenStream stream_TERM = new RewriteRuleTokenStream(
                adaptor, "token TERM");
        RewriteRuleSubtreeStream stream_declaratorList = new RewriteRuleSubtreeStream(
                adaptor, "rule declaratorList");
        RewriteRuleSubtreeStream stream_declarationSpecifiers = new RewriteRuleSubtreeStream(
                adaptor, "rule declarationSpecifiers");

        enter("declaration");

        try {
            // Grammar/CTFParser.g:330:1: ( ( declarationSpecifiers (
            // declaratorList )? TERM ) -> {inTypedef()}? ^( DECLARATION ^(
            // TYPEDEF declaratorList declarationSpecifiers ) ) -> ^(
            // DECLARATION declarationSpecifiers ( declaratorList )? ) | (
            // ctfSpecifier TERM ) )
            int alt13 = 2;
            int LA13_0 = input.LA(1);

            if ((((LA13_0 >= CONSTTOK) && (LA13_0 <= ENUMTOK))
                    || ((LA13_0 >= FLOATINGPOINTTOK) && (LA13_0 <= SIGNEDTOK))
                    || ((LA13_0 >= STRINGTOK) && (LA13_0 <= STRUCTTOK)) || ((LA13_0 >= TYPEDEFTOK) && (LA13_0 <= IMAGINARYTOK)))) {
                alt13 = 1;
            } else if ((LA13_0 == IDENTIFIER)
                    && ((inTypealiasAlias() || isTypeName(input.LT(1).getText())))) {
                alt13 = 1;
            } else if (((LA13_0 == EVENTTOK) || (LA13_0 == STREAMTOK)
                    || ((LA13_0 >= TRACETOK) && (LA13_0 <= TYPEALIASTOK)) || ((LA13_0 >= ENVTOK) && (LA13_0 <= CLOCKTOK)))) {
                alt13 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 13, 0,
                        input);

                throw nvae;
            }
            switch (alt13) {
            case 1:
            // Grammar/CTFParser.g:331:3: ( declarationSpecifiers (
            // declaratorList )? TERM )
            {
                // Grammar/CTFParser.g:331:3: ( declarationSpecifiers (
                // declaratorList )? TERM )
                // Grammar/CTFParser.g:331:4: declarationSpecifiers (
                // declaratorList )? TERM
                {
                    pushFollow(FOLLOW_declarationSpecifiers_in_declaration790);
                    declarationSpecifiers27 = declarationSpecifiers();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_declarationSpecifiers
                                .add(declarationSpecifiers27.getTree());
                    }
                    // Grammar/CTFParser.g:331:26: ( declaratorList )?
                    int alt12 = 2;
                    int LA12_0 = input.LA(1);

                    if (((LA12_0 == POINTER) || (LA12_0 == IDENTIFIER))) {
                        alt12 = 1;
                    }
                    switch (alt12) {
                    case 1:
                    // Grammar/CTFParser.g:331:26: declaratorList
                    {
                        pushFollow(FOLLOW_declaratorList_in_declaration792);
                        declaratorList28 = declaratorList();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_declaratorList.add(declaratorList28
                                    .getTree());
                        }

                    }
                        break;

                    }

                    TERM29 = (Token) match(input, TERM,
                            FOLLOW_TERM_in_declaration795);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_TERM.add(TERM29);
                    }

                }

                // AST REWRITE
                // elements: declaratorList, declaratorList,
                // declarationSpecifiers, declarationSpecifiers
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 334:3: -> {inTypedef()}? ^( DECLARATION ^( TYPEDEF
                    // declaratorList declarationSpecifiers ) )
                    if (inTypedef()) {
                        // Grammar/CTFParser.g:334:21: ^( DECLARATION ^( TYPEDEF
                        // declaratorList declarationSpecifiers ) )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(DECLARATION, "DECLARATION"),
                                    root_1);

                            // Grammar/CTFParser.g:334:35: ^( TYPEDEF
                            // declaratorList declarationSpecifiers )
                            {
                                CommonTree root_2 = (CommonTree) adaptor.nil();
                                root_2 = (CommonTree) adaptor.becomeRoot(
                                        adaptor.create(TYPEDEF, "TYPEDEF"),
                                        root_2);

                                adaptor.addChild(root_2,
                                        stream_declaratorList.nextTree());
                                adaptor.addChild(root_2,
                                        stream_declarationSpecifiers.nextTree());

                                adaptor.addChild(root_1, root_2);
                            }

                            adaptor.addChild(root_0, root_1);
                        }

                    } else // 335:3: -> ^( DECLARATION declarationSpecifiers (
                           // declaratorList )? )
                    {
                        // Grammar/CTFParser.g:335:6: ^( DECLARATION
                        // declarationSpecifiers ( declaratorList )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(DECLARATION, "DECLARATION"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_declarationSpecifiers.nextTree());
                            // Grammar/CTFParser.g:335:42: ( declaratorList )?
                            if (stream_declaratorList.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_declaratorList.nextTree());

                            }
                            stream_declaratorList.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 2:
            // Grammar/CTFParser.g:337:5: ( ctfSpecifier TERM )
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:337:5: ( ctfSpecifier TERM )
                // Grammar/CTFParser.g:337:6: ctfSpecifier TERM
                {
                    pushFollow(FOLLOW_ctfSpecifier_in_declaration844);
                    ctfSpecifier30 = ctfSpecifier();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        adaptor.addChild(root_0, ctfSpecifier30.getTree());
                    }
//                    TERM31 = (Token)
                            match(input, TERM,
                            FOLLOW_TERM_in_declaration846);
                    if (state.failed) {
                        return retval;
                    }

                }

            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("declaration");
                if (inTypedef()) {
                    typedefOff();
                }

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "declaration"

    public static class declarationSpecifiers_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "declarationSpecifiers"
    // Grammar/CTFParser.g:340:1: declarationSpecifiers : (
    // storageClassSpecifier | typeQualifier | typeSpecifier )+ -> ^(
    // TYPE_SPECIFIER_LIST ( typeQualifier )* ( typeSpecifier )* ) ;
    public final TSDLParser.declarationSpecifiers_return declarationSpecifiers()
            throws RecognitionException {
        TSDLParser.declarationSpecifiers_return retval = new TSDLParser.declarationSpecifiers_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.storageClassSpecifier_return storageClassSpecifier32 = null;

        TSDLParser.typeQualifier_return typeQualifier33 = null;

        TSDLParser.typeSpecifier_return typeSpecifier34 = null;

        RewriteRuleSubtreeStream stream_typeSpecifier = new RewriteRuleSubtreeStream(
                adaptor, "rule typeSpecifier");
        RewriteRuleSubtreeStream stream_typeQualifier = new RewriteRuleSubtreeStream(
                adaptor, "rule typeQualifier");
        RewriteRuleSubtreeStream stream_storageClassSpecifier = new RewriteRuleSubtreeStream(
                adaptor, "rule storageClassSpecifier");

        enter("declarationSpecifiers");

        try {
            // Grammar/CTFParser.g:348:1: ( ( storageClassSpecifier |
            // typeQualifier | typeSpecifier )+ -> ^( TYPE_SPECIFIER_LIST (
            // typeQualifier )* ( typeSpecifier )* ) )
            // Grammar/CTFParser.g:349:3: ( storageClassSpecifier |
            // typeQualifier | typeSpecifier )+
            {
                // Grammar/CTFParser.g:349:3: ( storageClassSpecifier |
                // typeQualifier | typeSpecifier )+
                int cnt14 = 0;
                loop14: do {
                    int alt14 = 4;
                    switch (input.LA(1)) {
                    case IDENTIFIER: {
                        int LA14_2 = input.LA(2);

                        if (((inTypealiasAlias() || isTypeName(input.LT(1)
                                .getText())))) {
                            alt14 = 3;
                        }

                    }
                        break;
                    case TYPEDEFTOK: {
                        alt14 = 1;
                    }
                        break;
                    case CONSTTOK: {
                        alt14 = 2;
                    }
                        break;
                    case CHARTOK:
                    case DOUBLETOK:
                    case ENUMTOK:
                    case FLOATINGPOINTTOK:
                    case FLOATTOK:
                    case INTEGERTOK:
                    case INTTOK:
                    case LONGTOK:
                    case SHORTTOK:
                    case SIGNEDTOK:
                    case STRINGTOK:
                    case STRUCTTOK:
                    case UNSIGNEDTOK:
                    case VARIANTTOK:
                    case VOIDTOK:
                    case BOOLTOK:
                    case COMPLEXTOK:
                    case IMAGINARYTOK: {
                        alt14 = 3;
                    }
                        break;

                    }

                    switch (alt14) {
                    case 1:
                    // Grammar/CTFParser.g:352:6: storageClassSpecifier
                    {
                        pushFollow(FOLLOW_storageClassSpecifier_in_declarationSpecifiers891);
                        storageClassSpecifier32 = storageClassSpecifier();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_storageClassSpecifier
                                    .add(storageClassSpecifier32.getTree());
                        }

                    }
                        break;
                    case 2:
                    // Grammar/CTFParser.g:353:6: typeQualifier
                    {
                        pushFollow(FOLLOW_typeQualifier_in_declarationSpecifiers898);
                        typeQualifier33 = typeQualifier();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_typeQualifier.add(typeQualifier33.getTree());
                        }

                    }
                        break;
                    case 3:
                    // Grammar/CTFParser.g:354:6: typeSpecifier
                    {
                        pushFollow(FOLLOW_typeSpecifier_in_declarationSpecifiers905);
                        typeSpecifier34 = typeSpecifier();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_typeSpecifier.add(typeSpecifier34.getTree());
                        }

                    }
                        break;

                    default:
                        if (cnt14 >= 1) {
                            break loop14;
                        }
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        EarlyExitException eee = new EarlyExitException(14,
                                input);
                        throw eee;
                    }
                    cnt14++;
                } while (true);

                // AST REWRITE
                // elements: typeQualifier, typeSpecifier
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 355:6: -> ^( TYPE_SPECIFIER_LIST ( typeQualifier )* (
                    // typeSpecifier )* )
                    {
                        // Grammar/CTFParser.g:355:9: ^( TYPE_SPECIFIER_LIST (
                        // typeQualifier )* ( typeSpecifier )* )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(TYPE_SPECIFIER_LIST,
                                            "TYPE_SPECIFIER_LIST"), root_1);

                            // Grammar/CTFParser.g:355:31: ( typeQualifier )*
                            while (stream_typeQualifier.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_typeQualifier.nextTree());

                            }
                            stream_typeQualifier.reset();
                            // Grammar/CTFParser.g:355:46: ( typeSpecifier )*
                            while (stream_typeSpecifier.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_typeSpecifier.nextTree());

                            }
                            stream_typeSpecifier.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("declarationSpecifiers");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "declarationSpecifiers"

    public static class declaratorList_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "declaratorList"
    // Grammar/CTFParser.g:358:1: declaratorList : declarator ( SEPARATOR
    // declarator )* -> ^( TYPE_DECLARATOR_LIST ( declarator )+ ) ;
    public final TSDLParser.declaratorList_return declaratorList()
            throws RecognitionException {
        TSDLParser.declaratorList_return retval = new TSDLParser.declaratorList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEPARATOR36 = null;
        TSDLParser.declarator_return declarator35 = null;

        TSDLParser.declarator_return declarator37 = null;

        CommonTree SEPARATOR36_tree = null;
        RewriteRuleTokenStream stream_SEPARATOR = new RewriteRuleTokenStream(
                adaptor, "token SEPARATOR");
        RewriteRuleSubtreeStream stream_declarator = new RewriteRuleSubtreeStream(
                adaptor, "rule declarator");

        enter("declaratorList");

        try {
            // Grammar/CTFParser.g:365:1: ( declarator ( SEPARATOR declarator )*
            // -> ^( TYPE_DECLARATOR_LIST ( declarator )+ ) )
            // Grammar/CTFParser.g:366:3: declarator ( SEPARATOR declarator )*
            {
                pushFollow(FOLLOW_declarator_in_declaratorList946);
                declarator35 = declarator();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_declarator.add(declarator35.getTree());
                }
                // Grammar/CTFParser.g:366:14: ( SEPARATOR declarator )*
                loop15: do {
                    int alt15 = 2;
                    int LA15_0 = input.LA(1);

                    if ((LA15_0 == SEPARATOR)) {
                        alt15 = 1;
                    }

                    switch (alt15) {
                    case 1:
                    // Grammar/CTFParser.g:366:15: SEPARATOR declarator
                    {
                        SEPARATOR36 = (Token) match(input, SEPARATOR,
                                FOLLOW_SEPARATOR_in_declaratorList949);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_SEPARATOR.add(SEPARATOR36);
                        }

                        pushFollow(FOLLOW_declarator_in_declaratorList951);
                        declarator37 = declarator();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_declarator.add(declarator37.getTree());
                        }

                    }
                        break;

                    default:
                        break loop15;
                    }
                } while (true);

                // AST REWRITE
                // elements: declarator
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 366:38: -> ^( TYPE_DECLARATOR_LIST ( declarator )+ )
                    {
                        // Grammar/CTFParser.g:366:41: ^( TYPE_DECLARATOR_LIST (
                        // declarator )+ )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(TYPE_DECLARATOR_LIST,
                                            "TYPE_DECLARATOR_LIST"), root_1);

                            if (!(stream_declarator.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_declarator.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_declarator.nextTree());

                            }
                            stream_declarator.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("declaratorList");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "declaratorList"

    public static class abstractDeclaratorList_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "abstractDeclaratorList"
    // Grammar/CTFParser.g:369:1: abstractDeclaratorList : abstractDeclarator (
    // SEPARATOR abstractDeclarator )* -> ^( TYPE_DECLARATOR_LIST (
    // abstractDeclarator )+ ) ;
    public final TSDLParser.abstractDeclaratorList_return abstractDeclaratorList()
            throws RecognitionException {
        TSDLParser.abstractDeclaratorList_return retval = new TSDLParser.abstractDeclaratorList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEPARATOR39 = null;
        TSDLParser.abstractDeclarator_return abstractDeclarator38 = null;

        TSDLParser.abstractDeclarator_return abstractDeclarator40 = null;

        CommonTree SEPARATOR39_tree = null;
        RewriteRuleTokenStream stream_SEPARATOR = new RewriteRuleTokenStream(
                adaptor, "token SEPARATOR");
        RewriteRuleSubtreeStream stream_abstractDeclarator = new RewriteRuleSubtreeStream(
                adaptor, "rule abstractDeclarator");

        enter("abstractDeclaratorList");

        try {
            // Grammar/CTFParser.g:376:1: ( abstractDeclarator ( SEPARATOR
            // abstractDeclarator )* -> ^( TYPE_DECLARATOR_LIST (
            // abstractDeclarator )+ ) )
            // Grammar/CTFParser.g:377:3: abstractDeclarator ( SEPARATOR
            // abstractDeclarator )*
            {
                pushFollow(FOLLOW_abstractDeclarator_in_abstractDeclaratorList985);
                abstractDeclarator38 = abstractDeclarator();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_abstractDeclarator.add(abstractDeclarator38
                            .getTree());
                }
                // Grammar/CTFParser.g:377:22: ( SEPARATOR abstractDeclarator )*
                loop16: do {
                    int alt16 = 2;
                    int LA16_0 = input.LA(1);

                    if ((LA16_0 == SEPARATOR)) {
                        alt16 = 1;
                    }

                    switch (alt16) {
                    case 1:
                    // Grammar/CTFParser.g:377:23: SEPARATOR abstractDeclarator
                    {
                        SEPARATOR39 = (Token) match(input, SEPARATOR,
                                FOLLOW_SEPARATOR_in_abstractDeclaratorList988);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_SEPARATOR.add(SEPARATOR39);
                        }

                        pushFollow(FOLLOW_abstractDeclarator_in_abstractDeclaratorList990);
                        abstractDeclarator40 = abstractDeclarator();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_abstractDeclarator.add(abstractDeclarator40
                                    .getTree());
                        }

                    }
                        break;

                    default:
                        break loop16;
                    }
                } while (true);

                // AST REWRITE
                // elements: abstractDeclarator
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 377:54: -> ^( TYPE_DECLARATOR_LIST ( abstractDeclarator
                    // )+ )
                    {
                        // Grammar/CTFParser.g:377:57: ^( TYPE_DECLARATOR_LIST (
                        // abstractDeclarator )+ )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(TYPE_DECLARATOR_LIST,
                                            "TYPE_DECLARATOR_LIST"), root_1);

                            if (!(stream_abstractDeclarator.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_abstractDeclarator.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_abstractDeclarator.nextTree());

                            }
                            stream_abstractDeclarator.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("abstractDeclaratorList");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "abstractDeclaratorList"

    public static class storageClassSpecifier_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "storageClassSpecifier"
    // Grammar/CTFParser.g:380:1: storageClassSpecifier : TYPEDEFTOK ;
    public final TSDLParser.storageClassSpecifier_return storageClassSpecifier()
            throws RecognitionException {
        TSDLParser.storageClassSpecifier_return retval = new TSDLParser.storageClassSpecifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token TYPEDEFTOK41 = null;

        CommonTree TYPEDEFTOK41_tree = null;

        try {
            // Grammar/CTFParser.g:380:23: ( TYPEDEFTOK )
            // Grammar/CTFParser.g:381:3: TYPEDEFTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                TYPEDEFTOK41 = (Token) match(input, TYPEDEFTOK,
                        FOLLOW_TYPEDEFTOK_in_storageClassSpecifier1014);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    TYPEDEFTOK41_tree = (CommonTree) adaptor
                            .create(TYPEDEFTOK41);
                    adaptor.addChild(root_0, TYPEDEFTOK41_tree);
                }
                if (state.backtracking == 0) {
                    typedefOn();
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "storageClassSpecifier"

    public static class typeSpecifier_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "typeSpecifier"
    // Grammar/CTFParser.g:384:1: typeSpecifier : ( FLOATTOK | INTTOK | LONGTOK
    // | SHORTTOK | SIGNEDTOK | UNSIGNEDTOK | CHARTOK | DOUBLETOK | VOIDTOK |
    // BOOLTOK | COMPLEXTOK | IMAGINARYTOK | structSpecifier | variantSpecifier
    // | enumSpecifier | ctfTypeSpecifier | {...}? => typedefName );
    public final TSDLParser.typeSpecifier_return typeSpecifier()
            throws RecognitionException {
        TSDLParser.typeSpecifier_return retval = new TSDLParser.typeSpecifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token FLOATTOK42 = null;
        Token INTTOK43 = null;
        Token LONGTOK44 = null;
        Token SHORTTOK45 = null;
        Token SIGNEDTOK46 = null;
        Token UNSIGNEDTOK47 = null;
        Token CHARTOK48 = null;
        Token DOUBLETOK49 = null;
        Token VOIDTOK50 = null;
        Token BOOLTOK51 = null;
        Token COMPLEXTOK52 = null;
        Token IMAGINARYTOK53 = null;
        TSDLParser.structSpecifier_return structSpecifier54 = null;

        TSDLParser.variantSpecifier_return variantSpecifier55 = null;

        TSDLParser.enumSpecifier_return enumSpecifier56 = null;

        TSDLParser.ctfTypeSpecifier_return ctfTypeSpecifier57 = null;

        TSDLParser.typedefName_return typedefName58 = null;

        CommonTree FLOATTOK42_tree = null;
        CommonTree INTTOK43_tree = null;
        CommonTree LONGTOK44_tree = null;
        CommonTree SHORTTOK45_tree = null;
        CommonTree SIGNEDTOK46_tree = null;
        CommonTree UNSIGNEDTOK47_tree = null;
        CommonTree CHARTOK48_tree = null;
        CommonTree DOUBLETOK49_tree = null;
        CommonTree VOIDTOK50_tree = null;
        CommonTree BOOLTOK51_tree = null;
        CommonTree COMPLEXTOK52_tree = null;
        CommonTree IMAGINARYTOK53_tree = null;

        enter("typeSpecifier");

        try {
            // Grammar/CTFParser.g:392:1: ( FLOATTOK | INTTOK | LONGTOK |
            // SHORTTOK | SIGNEDTOK | UNSIGNEDTOK | CHARTOK | DOUBLETOK |
            // VOIDTOK | BOOLTOK | COMPLEXTOK | IMAGINARYTOK | structSpecifier |
            // variantSpecifier | enumSpecifier | ctfTypeSpecifier | {...}? =>
            // typedefName )
            int alt17 = 17;
            alt17 = dfa17.predict(input);
            switch (alt17) {
            case 1:
            // Grammar/CTFParser.g:393:3: FLOATTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                FLOATTOK42 = (Token) match(input, FLOATTOK,
                        FOLLOW_FLOATTOK_in_typeSpecifier1040);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    FLOATTOK42_tree = (CommonTree) adaptor.create(FLOATTOK42);
                    adaptor.addChild(root_0, FLOATTOK42_tree);
                }

            }
                break;
            case 2:
            // Grammar/CTFParser.g:394:5: INTTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                INTTOK43 = (Token) match(input, INTTOK,
                        FOLLOW_INTTOK_in_typeSpecifier1046);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    INTTOK43_tree = (CommonTree) adaptor.create(INTTOK43);
                    adaptor.addChild(root_0, INTTOK43_tree);
                }

            }
                break;
            case 3:
            // Grammar/CTFParser.g:395:5: LONGTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                LONGTOK44 = (Token) match(input, LONGTOK,
                        FOLLOW_LONGTOK_in_typeSpecifier1052);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    LONGTOK44_tree = (CommonTree) adaptor.create(LONGTOK44);
                    adaptor.addChild(root_0, LONGTOK44_tree);
                }

            }
                break;
            case 4:
            // Grammar/CTFParser.g:396:5: SHORTTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                SHORTTOK45 = (Token) match(input, SHORTTOK,
                        FOLLOW_SHORTTOK_in_typeSpecifier1058);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    SHORTTOK45_tree = (CommonTree) adaptor.create(SHORTTOK45);
                    adaptor.addChild(root_0, SHORTTOK45_tree);
                }

            }
                break;
            case 5:
            // Grammar/CTFParser.g:397:5: SIGNEDTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                SIGNEDTOK46 = (Token) match(input, SIGNEDTOK,
                        FOLLOW_SIGNEDTOK_in_typeSpecifier1064);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    SIGNEDTOK46_tree = (CommonTree) adaptor.create(SIGNEDTOK46);
                    adaptor.addChild(root_0, SIGNEDTOK46_tree);
                }

            }
                break;
            case 6:
            // Grammar/CTFParser.g:398:5: UNSIGNEDTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                UNSIGNEDTOK47 = (Token) match(input, UNSIGNEDTOK,
                        FOLLOW_UNSIGNEDTOK_in_typeSpecifier1070);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    UNSIGNEDTOK47_tree = (CommonTree) adaptor
                            .create(UNSIGNEDTOK47);
                    adaptor.addChild(root_0, UNSIGNEDTOK47_tree);
                }

            }
                break;
            case 7:
            // Grammar/CTFParser.g:399:5: CHARTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                CHARTOK48 = (Token) match(input, CHARTOK,
                        FOLLOW_CHARTOK_in_typeSpecifier1076);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    CHARTOK48_tree = (CommonTree) adaptor.create(CHARTOK48);
                    adaptor.addChild(root_0, CHARTOK48_tree);
                }

            }
                break;
            case 8:
            // Grammar/CTFParser.g:400:5: DOUBLETOK
            {
                root_0 = (CommonTree) adaptor.nil();

                DOUBLETOK49 = (Token) match(input, DOUBLETOK,
                        FOLLOW_DOUBLETOK_in_typeSpecifier1082);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    DOUBLETOK49_tree = (CommonTree) adaptor.create(DOUBLETOK49);
                    adaptor.addChild(root_0, DOUBLETOK49_tree);
                }

            }
                break;
            case 9:
            // Grammar/CTFParser.g:401:5: VOIDTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                VOIDTOK50 = (Token) match(input, VOIDTOK,
                        FOLLOW_VOIDTOK_in_typeSpecifier1088);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    VOIDTOK50_tree = (CommonTree) adaptor.create(VOIDTOK50);
                    adaptor.addChild(root_0, VOIDTOK50_tree);
                }

            }
                break;
            case 10:
            // Grammar/CTFParser.g:402:5: BOOLTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                BOOLTOK51 = (Token) match(input, BOOLTOK,
                        FOLLOW_BOOLTOK_in_typeSpecifier1094);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    BOOLTOK51_tree = (CommonTree) adaptor.create(BOOLTOK51);
                    adaptor.addChild(root_0, BOOLTOK51_tree);
                }

            }
                break;
            case 11:
            // Grammar/CTFParser.g:403:5: COMPLEXTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                COMPLEXTOK52 = (Token) match(input, COMPLEXTOK,
                        FOLLOW_COMPLEXTOK_in_typeSpecifier1100);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    COMPLEXTOK52_tree = (CommonTree) adaptor
                            .create(COMPLEXTOK52);
                    adaptor.addChild(root_0, COMPLEXTOK52_tree);
                }

            }
                break;
            case 12:
            // Grammar/CTFParser.g:404:5: IMAGINARYTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                IMAGINARYTOK53 = (Token) match(input, IMAGINARYTOK,
                        FOLLOW_IMAGINARYTOK_in_typeSpecifier1106);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    IMAGINARYTOK53_tree = (CommonTree) adaptor
                            .create(IMAGINARYTOK53);
                    adaptor.addChild(root_0, IMAGINARYTOK53_tree);
                }

            }
                break;
            case 13:
            // Grammar/CTFParser.g:405:5: structSpecifier
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_structSpecifier_in_typeSpecifier1112);
                structSpecifier54 = structSpecifier();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, structSpecifier54.getTree());
                }

            }
                break;
            case 14:
            // Grammar/CTFParser.g:406:5: variantSpecifier
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_variantSpecifier_in_typeSpecifier1118);
                variantSpecifier55 = variantSpecifier();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, variantSpecifier55.getTree());
                }

            }
                break;
            case 15:
            // Grammar/CTFParser.g:407:5: enumSpecifier
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_enumSpecifier_in_typeSpecifier1124);
                enumSpecifier56 = enumSpecifier();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, enumSpecifier56.getTree());
                }

            }
                break;
            case 16:
            // Grammar/CTFParser.g:408:5: ctfTypeSpecifier
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_ctfTypeSpecifier_in_typeSpecifier1130);
                ctfTypeSpecifier57 = ctfTypeSpecifier();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, ctfTypeSpecifier57.getTree());
                }

            }
                break;
            case 17:
            // Grammar/CTFParser.g:409:5: {...}? => typedefName
            {
                root_0 = (CommonTree) adaptor.nil();

                if (!((inTypealiasAlias() || isTypeName(input.LT(1).getText())))) {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    throw new FailedPredicateException(input, "typeSpecifier",
                            "inTypealiasAlias() || isTypeName(input.LT(1).getText())");
                }
                pushFollow(FOLLOW_typedefName_in_typeSpecifier1140);
                typedefName58 = typedefName();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, typedefName58.getTree());
                }

            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("typeSpecifier");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "typeSpecifier"

    public static class typeQualifier_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "typeQualifier"
    // Grammar/CTFParser.g:412:1: typeQualifier : CONSTTOK ;
    public final TSDLParser.typeQualifier_return typeQualifier()
            throws RecognitionException {
        TSDLParser.typeQualifier_return retval = new TSDLParser.typeQualifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CONSTTOK59 = null;

        CommonTree CONSTTOK59_tree = null;

        enter("typeQualifier");

        try {
            // Grammar/CTFParser.g:420:1: ( CONSTTOK )
            // Grammar/CTFParser.g:421:3: CONSTTOK
            {
                root_0 = (CommonTree) adaptor.nil();

                CONSTTOK59 = (Token) match(input, CONSTTOK,
                        FOLLOW_CONSTTOK_in_typeQualifier1163);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    CONSTTOK59_tree = (CommonTree) adaptor.create(CONSTTOK59);
                    adaptor.addChild(root_0, CONSTTOK59_tree);
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("typeQualifier");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }

        return retval;
    }

    // $ANTLR end "typeQualifier"

    public static class alignAttribute_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "alignAttribute"
    // Grammar/CTFParser.g:424:1: alignAttribute : ALIGNTOK LPAREN
    // unaryExpression RPAREN -> ^( ALIGN unaryExpression ) ;
    public final TSDLParser.alignAttribute_return alignAttribute()
            throws RecognitionException {
        TSDLParser.alignAttribute_return retval = new TSDLParser.alignAttribute_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ALIGNTOK60 = null;
        Token LPAREN61 = null;
        Token RPAREN63 = null;
        TSDLParser.unaryExpression_return unaryExpression62 = null;

        CommonTree ALIGNTOK60_tree = null;
        CommonTree LPAREN61_tree = null;
        CommonTree RPAREN63_tree = null;
        RewriteRuleTokenStream stream_RPAREN = new RewriteRuleTokenStream(
                adaptor, "token RPAREN");
        RewriteRuleTokenStream stream_ALIGNTOK = new RewriteRuleTokenStream(
                adaptor, "token ALIGNTOK");
        RewriteRuleTokenStream stream_LPAREN = new RewriteRuleTokenStream(
                adaptor, "token LPAREN");
        RewriteRuleSubtreeStream stream_unaryExpression = new RewriteRuleSubtreeStream(
                adaptor, "rule unaryExpression");
        try {
            // Grammar/CTFParser.g:424:16: ( ALIGNTOK LPAREN unaryExpression
            // RPAREN -> ^( ALIGN unaryExpression ) )
            // Grammar/CTFParser.g:425:3: ALIGNTOK LPAREN unaryExpression RPAREN
            {
                ALIGNTOK60 = (Token) match(input, ALIGNTOK,
                        FOLLOW_ALIGNTOK_in_alignAttribute1176);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ALIGNTOK.add(ALIGNTOK60);
                }

                LPAREN61 = (Token) match(input, LPAREN,
                        FOLLOW_LPAREN_in_alignAttribute1178);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_LPAREN.add(LPAREN61);
                }

                pushFollow(FOLLOW_unaryExpression_in_alignAttribute1180);
                unaryExpression62 = unaryExpression();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_unaryExpression.add(unaryExpression62.getTree());
                }
                RPAREN63 = (Token) match(input, RPAREN,
                        FOLLOW_RPAREN_in_alignAttribute1182);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_RPAREN.add(RPAREN63);
                }

                // AST REWRITE
                // elements: unaryExpression
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 425:42: -> ^( ALIGN unaryExpression )
                    {
                        // Grammar/CTFParser.g:425:45: ^( ALIGN unaryExpression
                        // )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(ALIGN, "ALIGN"), root_1);

                            adaptor.addChild(root_1,
                                    stream_unaryExpression.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }

        return retval;
    }

    // $ANTLR end "alignAttribute"

    public static class structBody_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "structBody"
    // Grammar/CTFParser.g:429:1: structBody : LCURL (
    // structOrVariantDeclarationList )? RCURL -> ^( STRUCT_BODY (
    // structOrVariantDeclarationList )? ) ;
    public final TSDLParser.structBody_return structBody()
            throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        TSDLParser.structBody_return retval = new TSDLParser.structBody_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LCURL64 = null;
        Token RCURL66 = null;
        TSDLParser.structOrVariantDeclarationList_return structOrVariantDeclarationList65 = null;

        CommonTree LCURL64_tree = null;
        CommonTree RCURL66_tree = null;
        RewriteRuleTokenStream stream_LCURL = new RewriteRuleTokenStream(
                adaptor, "token LCURL");
        RewriteRuleTokenStream stream_RCURL = new RewriteRuleTokenStream(
                adaptor, "token RCURL");
        RewriteRuleSubtreeStream stream_structOrVariantDeclarationList = new RewriteRuleSubtreeStream(
                adaptor, "rule structOrVariantDeclarationList");

        enter("structBody");
        debug_print("Scope push " + Symbols_stack.size());
        ((Symbols_scope) Symbols_stack.peek()).types = new HashSet<String>();

        try {
            // Grammar/CTFParser.g:440:1: ( LCURL (
            // structOrVariantDeclarationList )? RCURL -> ^( STRUCT_BODY (
            // structOrVariantDeclarationList )? ) )
            // Grammar/CTFParser.g:441:3: LCURL ( structOrVariantDeclarationList
            // )? RCURL
            {
                LCURL64 = (Token) match(input, LCURL,
                        FOLLOW_LCURL_in_structBody1223);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_LCURL.add(LCURL64);
                }

                // Grammar/CTFParser.g:441:9: ( structOrVariantDeclarationList
                // )?
                int alt18 = 2;
                int LA18_0 = input.LA(1);

                if ((((LA18_0 >= CONSTTOK) && (LA18_0 <= ENUMTOK))
                        || ((LA18_0 >= FLOATINGPOINTTOK) && (LA18_0 <= SIGNEDTOK))
                        || ((LA18_0 >= STRINGTOK) && (LA18_0 <= STRUCTTOK)) || ((LA18_0 >= TYPEDEFTOK) && (LA18_0 <= IMAGINARYTOK)))) {
                    alt18 = 1;
                } else if ((LA18_0 == IDENTIFIER)
                        && ((inTypealiasAlias() || isTypeName(input.LT(1)
                                .getText())))) {
                    alt18 = 1;
                } else if ((LA18_0 == TYPEALIASTOK)) {
                    alt18 = 1;
                }
                switch (alt18) {
                case 1:
                // Grammar/CTFParser.g:441:9: structOrVariantDeclarationList
                {
                    pushFollow(FOLLOW_structOrVariantDeclarationList_in_structBody1225);
                    structOrVariantDeclarationList65 = structOrVariantDeclarationList();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_structOrVariantDeclarationList
                                .add(structOrVariantDeclarationList65.getTree());
                    }

                }
                    break;

                }

                RCURL66 = (Token) match(input, RCURL,
                        FOLLOW_RCURL_in_structBody1228);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_RCURL.add(RCURL66);
                }

                // AST REWRITE
                // elements: structOrVariantDeclarationList
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 441:47: -> ^( STRUCT_BODY (
                    // structOrVariantDeclarationList )? )
                    {
                        // Grammar/CTFParser.g:441:50: ^( STRUCT_BODY (
                        // structOrVariantDeclarationList )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(STRUCT_BODY, "STRUCT_BODY"),
                                    root_1);

                            // Grammar/CTFParser.g:441:64: (
                            // structOrVariantDeclarationList )?
                            if (stream_structOrVariantDeclarationList.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_structOrVariantDeclarationList
                                                .nextTree());

                            }
                            stream_structOrVariantDeclarationList.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print("Scope pop " + Symbols_stack.size());
                exit("structBody");

            }
        }

        catch (RecognitionException e) {
            throw e;
        } finally {
            Symbols_stack.pop();

        }
        return retval;
    }

    // $ANTLR end "structBody"

    public static class structSpecifier_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "structSpecifier"
    // Grammar/CTFParser.g:446:1: structSpecifier : STRUCTTOK ( ( structName (
    // alignAttribute | ( structBody ( alignAttribute | ) ) | ) ) | ( structBody
    // ( alignAttribute | ) ) ) -> ^( STRUCT ( structName )? ( structBody )? (
    // alignAttribute )? ) ;
    public final TSDLParser.structSpecifier_return structSpecifier()
            throws RecognitionException {
        TSDLParser.structSpecifier_return retval = new TSDLParser.structSpecifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token STRUCTTOK67 = null;
        TSDLParser.structName_return structName68 = null;

        TSDLParser.alignAttribute_return alignAttribute69 = null;

        TSDLParser.structBody_return structBody70 = null;

        TSDLParser.alignAttribute_return alignAttribute71 = null;

        TSDLParser.structBody_return structBody72 = null;

        TSDLParser.alignAttribute_return alignAttribute73 = null;

        CommonTree STRUCTTOK67_tree = null;
        RewriteRuleTokenStream stream_STRUCTTOK = new RewriteRuleTokenStream(
                adaptor, "token STRUCTTOK");
        RewriteRuleSubtreeStream stream_structName = new RewriteRuleSubtreeStream(
                adaptor, "rule structName");
        RewriteRuleSubtreeStream stream_structBody = new RewriteRuleSubtreeStream(
                adaptor, "rule structBody");
        RewriteRuleSubtreeStream stream_alignAttribute = new RewriteRuleSubtreeStream(
                adaptor, "rule alignAttribute");

        enter("structSpecifier");

        try {
            // Grammar/CTFParser.g:453:1: ( STRUCTTOK ( ( structName (
            // alignAttribute | ( structBody ( alignAttribute | ) ) | ) ) | (
            // structBody ( alignAttribute | ) ) ) -> ^( STRUCT ( structName )?
            // ( structBody )? ( alignAttribute )? ) )
            // Grammar/CTFParser.g:454:3: STRUCTTOK ( ( structName (
            // alignAttribute | ( structBody ( alignAttribute | ) ) | ) ) | (
            // structBody ( alignAttribute | ) ) )
            {
                STRUCTTOK67 = (Token) match(input, STRUCTTOK,
                        FOLLOW_STRUCTTOK_in_structSpecifier1266);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_STRUCTTOK.add(STRUCTTOK67);
                }

                // Grammar/CTFParser.g:455:3: ( ( structName ( alignAttribute |
                // ( structBody ( alignAttribute | ) ) | ) ) | ( structBody (
                // alignAttribute | ) ) )
                int alt22 = 2;
                int LA22_0 = input.LA(1);

                if ((LA22_0 == IDENTIFIER)) {
                    alt22 = 1;
                } else if ((LA22_0 == LCURL)) {
                    alt22 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            22, 0, input);

                    throw nvae;
                }
                switch (alt22) {
                case 1:
                // Grammar/CTFParser.g:457:5: ( structName ( alignAttribute | (
                // structBody ( alignAttribute | ) ) | ) )
                {
                    // Grammar/CTFParser.g:457:5: ( structName ( alignAttribute
                    // | ( structBody ( alignAttribute | ) ) | ) )
                    // Grammar/CTFParser.g:458:6: structName ( alignAttribute |
                    // ( structBody ( alignAttribute | ) ) | )
                    {
                        pushFollow(FOLLOW_structName_in_structSpecifier1288);
                        structName68 = structName();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_structName.add(structName68.getTree());
                        }
                        // Grammar/CTFParser.g:459:6: ( alignAttribute | (
                        // structBody ( alignAttribute | ) ) | )
                        int alt20 = 3;
                        switch (input.LA(1)) {
                        case ALIGNTOK: {
                            alt20 = 1;
                        }
                            break;
                        case LCURL: {
                            switch (input.LA(2)) {
                            case CONSTTOK:
                            case CHARTOK:
                            case DOUBLETOK:
                            case ENUMTOK:
                            case FLOATINGPOINTTOK:
                            case FLOATTOK:
                            case INTEGERTOK:
                            case INTTOK:
                            case LONGTOK:
                            case SHORTTOK:
                            case STRUCTTOK:
                            case TYPEALIASTOK:
                            case TYPEDEFTOK:
                            case UNSIGNEDTOK:
                            case VARIANTTOK:
                            case VOIDTOK:
                            case BOOLTOK:
                            case COMPLEXTOK:
                            case IMAGINARYTOK:
                            case RCURL: {
                                alt20 = 2;
                            }
                                break;
                            case SIGNEDTOK: {
                                int LA20_5 = input.LA(3);

                                if ((((LA20_5 >= CONSTTOK) && (LA20_5 <= ENUMTOK))
                                        || ((LA20_5 >= FLOATINGPOINTTOK) && (LA20_5 <= SIGNEDTOK))
                                        || ((LA20_5 >= STRINGTOK) && (LA20_5 <= STRUCTTOK))
                                        || ((LA20_5 >= TYPEDEFTOK) && (LA20_5 <= IMAGINARYTOK))
                                        || (LA20_5 == POINTER) || (LA20_5 == IDENTIFIER))) {
                                    alt20 = 2;
                                } else if (((LA20_5 == SEPARATOR)
                                        || (LA20_5 == ASSIGNMENT) || (LA20_5 == RCURL))) {
                                    alt20 = 3;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return retval;
                                    }
                                    NoViableAltException nvae = new NoViableAltException(
                                            "", 20, 5, input);

                                    throw nvae;
                                }
                            }
                                break;
                            case STRINGTOK: {
                                int LA20_6 = input.LA(3);

                                if ((((LA20_6 >= CONSTTOK) && (LA20_6 <= ENUMTOK))
                                        || ((LA20_6 >= FLOATINGPOINTTOK) && (LA20_6 <= SIGNEDTOK))
                                        || ((LA20_6 >= STRINGTOK) && (LA20_6 <= STRUCTTOK))
                                        || ((LA20_6 >= TYPEDEFTOK) && (LA20_6 <= IMAGINARYTOK))
                                        || (LA20_6 == LCURL)
                                        || (LA20_6 == POINTER) || (LA20_6 == IDENTIFIER))) {
                                    alt20 = 2;
                                } else if (((LA20_6 == SEPARATOR)
                                        || (LA20_6 == ASSIGNMENT) || (LA20_6 == RCURL))) {
                                    alt20 = 3;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return retval;
                                    }
                                    NoViableAltException nvae = new NoViableAltException(
                                            "", 20, 6, input);

                                    throw nvae;
                                }
                            }
                                break;
                            case IDENTIFIER: {
                                int LA20_7 = input.LA(3);

                                if (((LA20_7 == SEPARATOR)
                                        || (LA20_7 == ASSIGNMENT) || (LA20_7 == RCURL))) {
                                    alt20 = 3;
                                } else if ((((LA20_7 >= CONSTTOK) && (LA20_7 <= ENUMTOK))
                                        || ((LA20_7 >= FLOATINGPOINTTOK) && (LA20_7 <= SIGNEDTOK))
                                        || ((LA20_7 >= STRINGTOK) && (LA20_7 <= STRUCTTOK))
                                        || ((LA20_7 >= TYPEDEFTOK) && (LA20_7 <= IMAGINARYTOK))
                                        || (LA20_7 == POINTER) || (LA20_7 == IDENTIFIER))) {
                                    alt20 = 2;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return retval;
                                    }
                                    NoViableAltException nvae = new NoViableAltException(
                                            "", 20, 7, input);

                                    throw nvae;
                                }
                            }
                                break;
                            case ALIGNTOK:
                            case EVENTTOK:
                            case STRING_LITERAL: {
                                alt20 = 3;
                            }
                                break;
                            default:
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return retval;
                                }
                                NoViableAltException nvae = new NoViableAltException(
                                        "", 20, 2, input);

                                throw nvae;
                            }

                        }
                            break;
                        case EOF:
                        case CONSTTOK:
                        case CHARTOK:
                        case DOUBLETOK:
                        case ENUMTOK:
                        case FLOATINGPOINTTOK:
                        case FLOATTOK:
                        case INTEGERTOK:
                        case INTTOK:
                        case LONGTOK:
                        case SHORTTOK:
                        case SIGNEDTOK:
                        case STRINGTOK:
                        case STRUCTTOK:
                        case TYPEDEFTOK:
                        case UNSIGNEDTOK:
                        case VARIANTTOK:
                        case VOIDTOK:
                        case BOOLTOK:
                        case COMPLEXTOK:
                        case IMAGINARYTOK:
                        case TYPE_ASSIGNMENT:
                        case LPAREN:
                        case TERM:
                        case POINTER:
                        case IDENTIFIER: {
                            alt20 = 3;
                        }
                            break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return retval;
                            }
                            NoViableAltException nvae = new NoViableAltException(
                                    "", 20, 0, input);

                            throw nvae;
                        }

                        switch (alt20) {
                        case 1:
                        // Grammar/CTFParser.g:460:8: alignAttribute
                        {
                            pushFollow(FOLLOW_alignAttribute_in_structSpecifier1304);
                            alignAttribute69 = alignAttribute();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_alignAttribute.add(alignAttribute69
                                        .getTree());
                            }

                        }
                            break;
                        case 2:
                        // Grammar/CTFParser.g:462:8: ( structBody (
                        // alignAttribute | ) )
                        {
                            // Grammar/CTFParser.g:462:8: ( structBody (
                            // alignAttribute | ) )
                            // Grammar/CTFParser.g:463:10: structBody (
                            // alignAttribute | )
                            {
                                pushFollow(FOLLOW_structBody_in_structSpecifier1333);
                                structBody70 = structBody();

                                state._fsp--;
                                if (state.failed) {
                                    return retval;
                                }
                                if (state.backtracking == 0) {
                                    stream_structBody.add(structBody70
                                            .getTree());
                                }
                                // Grammar/CTFParser.g:464:10: ( alignAttribute
                                // | )
                                int alt19 = 2;
                                int LA19_0 = input.LA(1);

                                if ((LA19_0 == ALIGNTOK)) {
                                    alt19 = 1;
                                } else if (((LA19_0 == EOF)
                                        || ((LA19_0 >= CONSTTOK) && (LA19_0 <= ENUMTOK))
                                        || ((LA19_0 >= FLOATINGPOINTTOK) && (LA19_0 <= SIGNEDTOK))
                                        || ((LA19_0 >= STRINGTOK) && (LA19_0 <= STRUCTTOK))
                                        || ((LA19_0 >= TYPEDEFTOK) && (LA19_0 <= IMAGINARYTOK))
                                        || (LA19_0 == TYPE_ASSIGNMENT)
                                        || (LA19_0 == LPAREN)
                                        || (LA19_0 == LCURL)
                                        || ((LA19_0 >= TERM) && (LA19_0 <= POINTER)) || (LA19_0 == IDENTIFIER))) {
                                    alt19 = 2;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return retval;
                                    }
                                    NoViableAltException nvae = new NoViableAltException(
                                            "", 19, 0, input);

                                    throw nvae;
                                }
                                switch (alt19) {
                                case 1:
                                // Grammar/CTFParser.g:465:11: alignAttribute
                                {
                                    pushFollow(FOLLOW_alignAttribute_in_structSpecifier1358);
                                    alignAttribute71 = alignAttribute();

                                    state._fsp--;
                                    if (state.failed) {
                                        return retval;
                                    }
                                    if (state.backtracking == 0) {
                                        stream_alignAttribute
                                                .add(alignAttribute71.getTree());
                                    }

                                }
                                    break;
                                case 2:
                                // Grammar/CTFParser.g:468:10:
                                    break;

                                }

                            }

                        }
                            break;
                        case 3:
                        // Grammar/CTFParser.g:472:6:
                            break;

                        }

                    }

                }
                    break;
                case 2:
                // Grammar/CTFParser.g:476:5: ( structBody ( alignAttribute | )
                // )
                {
                    // Grammar/CTFParser.g:476:5: ( structBody ( alignAttribute
                    // | ) )
                    // Grammar/CTFParser.g:477:7: structBody ( alignAttribute |
                    // )
                    {
                        pushFollow(FOLLOW_structBody_in_structSpecifier1456);
                        structBody72 = structBody();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_structBody.add(structBody72.getTree());
                        }
                        // Grammar/CTFParser.g:478:7: ( alignAttribute | )
                        int alt21 = 2;
                        int LA21_0 = input.LA(1);

                        if ((LA21_0 == ALIGNTOK)) {
                            alt21 = 1;
                        } else if (((LA21_0 == EOF)
                                || ((LA21_0 >= CONSTTOK) && (LA21_0 <= ENUMTOK))
                                || ((LA21_0 >= FLOATINGPOINTTOK) && (LA21_0 <= SIGNEDTOK))
                                || ((LA21_0 >= STRINGTOK) && (LA21_0 <= STRUCTTOK))
                                || ((LA21_0 >= TYPEDEFTOK) && (LA21_0 <= IMAGINARYTOK))
                                || (LA21_0 == TYPE_ASSIGNMENT)
                                || (LA21_0 == LPAREN) || (LA21_0 == LCURL)
                                || ((LA21_0 >= TERM) && (LA21_0 <= POINTER)) || (LA21_0 == IDENTIFIER))) {
                            alt21 = 2;
                        } else {
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return retval;
                            }
                            NoViableAltException nvae = new NoViableAltException(
                                    "", 21, 0, input);

                            throw nvae;
                        }
                        switch (alt21) {
                        case 1:
                        // Grammar/CTFParser.g:479:9: alignAttribute
                        {
                            pushFollow(FOLLOW_alignAttribute_in_structSpecifier1474);
                            alignAttribute73 = alignAttribute();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_alignAttribute.add(alignAttribute73
                                        .getTree());
                            }

                        }
                            break;
                        case 2:
                            break;

                        }

                    }

                }
                    break;

                }

                // AST REWRITE
                // elements: alignAttribute, structName, structBody
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 484:5: -> ^( STRUCT ( structName )? ( structBody )? (
                    // alignAttribute )? )
                    {
                        // Grammar/CTFParser.g:484:8: ^( STRUCT ( structName )?
                        // ( structBody )? ( alignAttribute )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(STRUCT, "STRUCT"), root_1);

                            // Grammar/CTFParser.g:484:17: ( structName )?
                            if (stream_structName.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_structName.nextTree());

                            }
                            stream_structName.reset();
                            // Grammar/CTFParser.g:484:29: ( structBody )?
                            if (stream_structBody.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_structBody.nextTree());

                            }
                            stream_structBody.reset();
                            // Grammar/CTFParser.g:484:41: ( alignAttribute )?
                            if (stream_alignAttribute.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_alignAttribute.nextTree());

                            }
                            stream_alignAttribute.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("structSpecifier");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "structSpecifier"

    public static class structName_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "structName"
    // Grammar/CTFParser.g:487:1: structName : IDENTIFIER -> ^( STRUCT_NAME
    // IDENTIFIER ) ;
    public final TSDLParser.structName_return structName()
            throws RecognitionException {
        TSDLParser.structName_return retval = new TSDLParser.structName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENTIFIER74 = null;

        CommonTree IDENTIFIER74_tree = null;
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(
                adaptor, "token IDENTIFIER");

        enter("structName");

        try {
            // Grammar/CTFParser.g:495:1: ( IDENTIFIER -> ^( STRUCT_NAME
            // IDENTIFIER ) )
            // Grammar/CTFParser.g:496:3: IDENTIFIER
            {
                IDENTIFIER74 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_structName1550);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_IDENTIFIER.add(IDENTIFIER74);
                }

                // AST REWRITE
                // elements: IDENTIFIER
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 496:14: -> ^( STRUCT_NAME IDENTIFIER )
                    {
                        // Grammar/CTFParser.g:496:17: ^( STRUCT_NAME IDENTIFIER
                        // )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(STRUCT_NAME, "STRUCT_NAME"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_IDENTIFIER.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("structName");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "structName"

    public static class structOrVariantDeclarationList_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "structOrVariantDeclarationList"
    // Grammar/CTFParser.g:499:1: structOrVariantDeclarationList : (
    // structOrVariantDeclaration )+ ;
    public final TSDLParser.structOrVariantDeclarationList_return structOrVariantDeclarationList()
            throws RecognitionException {
        TSDLParser.structOrVariantDeclarationList_return retval = new TSDLParser.structOrVariantDeclarationList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.structOrVariantDeclaration_return structOrVariantDeclaration75 = null;

        enter("structOrVariantDeclarationList");

        try {
            // Grammar/CTFParser.g:506:1: ( ( structOrVariantDeclaration )+ )
            // Grammar/CTFParser.g:507:3: ( structOrVariantDeclaration )+
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:507:3: ( structOrVariantDeclaration )+
                int cnt23 = 0;
                loop23: do {
                    int alt23 = 2;
                    int LA23_0 = input.LA(1);

                    if ((((LA23_0 >= CONSTTOK) && (LA23_0 <= ENUMTOK))
                            || ((LA23_0 >= FLOATINGPOINTTOK) && (LA23_0 <= SIGNEDTOK))
                            || ((LA23_0 >= STRINGTOK) && (LA23_0 <= STRUCTTOK)) || ((LA23_0 >= TYPEDEFTOK) && (LA23_0 <= IMAGINARYTOK)))) {
                        alt23 = 1;
                    } else if ((LA23_0 == IDENTIFIER)
                            && ((inTypealiasAlias() || isTypeName(input.LT(1)
                                    .getText())))) {
                        alt23 = 1;
                    } else if ((LA23_0 == TYPEALIASTOK)) {
                        alt23 = 1;
                    }

                    switch (alt23) {
                    case 1:
                    // Grammar/CTFParser.g:507:3: structOrVariantDeclaration
                    {
                        pushFollow(FOLLOW_structOrVariantDeclaration_in_structOrVariantDeclarationList1581);
                        structOrVariantDeclaration75 = structOrVariantDeclaration();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    structOrVariantDeclaration75.getTree());
                        }

                    }
                        break;

                    default:
                        if (cnt23 >= 1) {
                            break loop23;
                        }
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        EarlyExitException eee = new EarlyExitException(23,
                                input);
                        throw eee;
                    }
                    cnt23++;
                } while (true);

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("structOrVariantDeclarationList");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "structOrVariantDeclarationList"

    public static class structOrVariantDeclaration_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "structOrVariantDeclaration"
    // Grammar/CTFParser.g:510:1: structOrVariantDeclaration : ( (
    // declarationSpecifiers ({...}? => declaratorList -> ^( TYPEDEF
    // declaratorList declarationSpecifiers ) | structOrVariantDeclaratorList ->
    // ^( SV_DECLARATION declarationSpecifiers structOrVariantDeclaratorList ) )
    // ) | typealiasDecl -> typealiasDecl ) TERM ;
    public final TSDLParser.structOrVariantDeclaration_return structOrVariantDeclaration()
            throws RecognitionException {
        TSDLParser.structOrVariantDeclaration_return retval = new TSDLParser.structOrVariantDeclaration_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token TERM80 = null;
        TSDLParser.declarationSpecifiers_return declarationSpecifiers76 = null;

        TSDLParser.declaratorList_return declaratorList77 = null;

        TSDLParser.structOrVariantDeclaratorList_return structOrVariantDeclaratorList78 = null;

        TSDLParser.typealiasDecl_return typealiasDecl79 = null;

        CommonTree TERM80_tree = null;
        RewriteRuleTokenStream stream_TERM = new RewriteRuleTokenStream(
                adaptor, "token TERM");
        RewriteRuleSubtreeStream stream_declaratorList = new RewriteRuleSubtreeStream(
                adaptor, "rule declaratorList");
        RewriteRuleSubtreeStream stream_typealiasDecl = new RewriteRuleSubtreeStream(
                adaptor, "rule typealiasDecl");
        RewriteRuleSubtreeStream stream_declarationSpecifiers = new RewriteRuleSubtreeStream(
                adaptor, "rule declarationSpecifiers");
        RewriteRuleSubtreeStream stream_structOrVariantDeclaratorList = new RewriteRuleSubtreeStream(
                adaptor, "rule structOrVariantDeclaratorList");

        enter("structOrVariantDeclaration");

        try {
            // Grammar/CTFParser.g:517:1: ( ( ( declarationSpecifiers ({...}? =>
            // declaratorList -> ^( TYPEDEF declaratorList declarationSpecifiers
            // ) | structOrVariantDeclaratorList -> ^( SV_DECLARATION
            // declarationSpecifiers structOrVariantDeclaratorList ) ) ) |
            // typealiasDecl -> typealiasDecl ) TERM )
            // Grammar/CTFParser.g:518:3: ( ( declarationSpecifiers ({...}? =>
            // declaratorList -> ^( TYPEDEF declaratorList declarationSpecifiers
            // ) | structOrVariantDeclaratorList -> ^( SV_DECLARATION
            // declarationSpecifiers structOrVariantDeclaratorList ) ) ) |
            // typealiasDecl -> typealiasDecl ) TERM
            {
                // Grammar/CTFParser.g:518:3: ( ( declarationSpecifiers ({...}?
                // => declaratorList -> ^( TYPEDEF declaratorList
                // declarationSpecifiers ) | structOrVariantDeclaratorList -> ^(
                // SV_DECLARATION declarationSpecifiers
                // structOrVariantDeclaratorList ) ) ) | typealiasDecl ->
                // typealiasDecl )
                int alt25 = 2;
                int LA25_0 = input.LA(1);

                if ((((LA25_0 >= CONSTTOK) && (LA25_0 <= ENUMTOK))
                        || ((LA25_0 >= FLOATINGPOINTTOK) && (LA25_0 <= SIGNEDTOK))
                        || ((LA25_0 >= STRINGTOK) && (LA25_0 <= STRUCTTOK)) || ((LA25_0 >= TYPEDEFTOK) && (LA25_0 <= IMAGINARYTOK)))) {
                    alt25 = 1;
                } else if ((LA25_0 == IDENTIFIER)
                        && ((inTypealiasAlias() || isTypeName(input.LT(1)
                                .getText())))) {
                    alt25 = 1;
                } else if ((LA25_0 == TYPEALIASTOK)) {
                    alt25 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            25, 0, input);

                    throw nvae;
                }
                switch (alt25) {
                case 1:
                // Grammar/CTFParser.g:519:4: ( declarationSpecifiers ({...}? =>
                // declaratorList -> ^( TYPEDEF declaratorList
                // declarationSpecifiers ) | structOrVariantDeclaratorList -> ^(
                // SV_DECLARATION declarationSpecifiers
                // structOrVariantDeclaratorList ) ) )
                {
                    // Grammar/CTFParser.g:519:4: ( declarationSpecifiers
                    // ({...}? => declaratorList -> ^( TYPEDEF declaratorList
                    // declarationSpecifiers ) | structOrVariantDeclaratorList
                    // -> ^( SV_DECLARATION declarationSpecifiers
                    // structOrVariantDeclaratorList ) ) )
                    // Grammar/CTFParser.g:520:5: declarationSpecifiers ({...}?
                    // => declaratorList -> ^( TYPEDEF declaratorList
                    // declarationSpecifiers ) | structOrVariantDeclaratorList
                    // -> ^( SV_DECLARATION declarationSpecifiers
                    // structOrVariantDeclaratorList ) )
                    {
                        pushFollow(FOLLOW_declarationSpecifiers_in_structOrVariantDeclaration1619);
                        declarationSpecifiers76 = declarationSpecifiers();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_declarationSpecifiers
                                    .add(declarationSpecifiers76.getTree());
                        }
                        // Grammar/CTFParser.g:521:7: ({...}? => declaratorList
                        // -> ^( TYPEDEF declaratorList declarationSpecifiers )
                        // | structOrVariantDeclaratorList -> ^( SV_DECLARATION
                        // declarationSpecifiers structOrVariantDeclaratorList )
                        // )
                        int alt24 = 2;
                        alt24 = dfa24.predict(input);
                        switch (alt24) {
                        case 1:
                        // Grammar/CTFParser.g:523:9: {...}? => declaratorList
                        {
                            if (!((inTypedef()))) {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return retval;
                                }
                                throw new FailedPredicateException(input,
                                        "structOrVariantDeclaration",
                                        "inTypedef()");
                            }
                            pushFollow(FOLLOW_declaratorList_in_structOrVariantDeclaration1651);
                            declaratorList77 = declaratorList();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_declaratorList.add(declaratorList77
                                        .getTree());
                            }
                            if (state.backtracking == 0) {
                                typedefOff();
                            }

                            // AST REWRITE
                            // elements: declarationSpecifiers, declaratorList
                            // token labels:
                            // rule labels: retval
                            // token list labels:
                            // rule list labels:
                            // wildcard labels:
                            if (state.backtracking == 0) {
                                retval.tree = root_0;
//                                RewriteRuleSubtreeStream stream_retval =
                                        new RewriteRuleSubtreeStream(
                                        adaptor, "rule retval",
                                        retval != null ? retval.tree : null);

                                root_0 = (CommonTree) adaptor.nil();
                                // 524:11: -> ^( TYPEDEF declaratorList
                                // declarationSpecifiers )
                                {
                                    // Grammar/CTFParser.g:524:14: ^( TYPEDEF
                                    // declaratorList declarationSpecifiers )
                                    {
                                        CommonTree root_1 = (CommonTree) adaptor
                                                .nil();
                                        root_1 = (CommonTree) adaptor
                                                .becomeRoot(adaptor.create(
                                                        TYPEDEF, "TYPEDEF"),
                                                        root_1);

                                        adaptor.addChild(root_1,
                                                stream_declaratorList
                                                        .nextTree());
                                        adaptor.addChild(root_1,
                                                stream_declarationSpecifiers
                                                        .nextTree());

                                        adaptor.addChild(root_0, root_1);
                                    }

                                }

                                retval.tree = root_0;
                            }
                        }
                            break;
                        case 2:
                        // Grammar/CTFParser.g:525:11:
                        // structOrVariantDeclaratorList
                        {
                            pushFollow(FOLLOW_structOrVariantDeclaratorList_in_structOrVariantDeclaration1685);
                            structOrVariantDeclaratorList78 = structOrVariantDeclaratorList();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_structOrVariantDeclaratorList
                                        .add(structOrVariantDeclaratorList78
                                                .getTree());
                            }

                            // AST REWRITE
                            // elements: structOrVariantDeclaratorList,
                            // declarationSpecifiers
                            // token labels:
                            // rule labels: retval
                            // token list labels:
                            // rule list labels:
                            // wildcard labels:
                            if (state.backtracking == 0) {
                                retval.tree = root_0;
//                                RewriteRuleSubtreeStream stream_retval =
                                        new RewriteRuleSubtreeStream(
                                        adaptor, "rule retval",
                                        retval != null ? retval.tree : null);

                                root_0 = (CommonTree) adaptor.nil();
                                // 526:11: -> ^( SV_DECLARATION
                                // declarationSpecifiers
                                // structOrVariantDeclaratorList )
                                {
                                    // Grammar/CTFParser.g:526:14: ^(
                                    // SV_DECLARATION declarationSpecifiers
                                    // structOrVariantDeclaratorList )
                                    {
                                        CommonTree root_1 = (CommonTree) adaptor
                                                .nil();
                                        root_1 = (CommonTree) adaptor
                                                .becomeRoot(adaptor.create(
                                                        SV_DECLARATION,
                                                        "SV_DECLARATION"),
                                                        root_1);

                                        adaptor.addChild(root_1,
                                                stream_declarationSpecifiers
                                                        .nextTree());
                                        adaptor.addChild(root_1,
                                                stream_structOrVariantDeclaratorList
                                                        .nextTree());

                                        adaptor.addChild(root_0, root_1);
                                    }

                                }

                                retval.tree = root_0;
                            }
                        }
                            break;

                        }

                    }

                }
                    break;
                case 2:
                // Grammar/CTFParser.g:531:5: typealiasDecl
                {
                    pushFollow(FOLLOW_typealiasDecl_in_structOrVariantDeclaration1735);
                    typealiasDecl79 = typealiasDecl();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_typealiasDecl.add(typealiasDecl79.getTree());
                    }

                    // AST REWRITE
                    // elements: typealiasDecl
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if (state.backtracking == 0) {
                        retval.tree = root_0;
//                        RewriteRuleSubtreeStream stream_retval =
                                new RewriteRuleSubtreeStream(
                                adaptor, "rule retval",
                                retval != null ? retval.tree : null);

                        root_0 = (CommonTree) adaptor.nil();
                        // 531:19: -> typealiasDecl
                        {
                            adaptor.addChild(root_0,
                                    stream_typealiasDecl.nextTree());

                        }

                        retval.tree = root_0;
                    }
                }
                    break;

                }

                TERM80 = (Token) match(input, TERM,
                        FOLLOW_TERM_in_structOrVariantDeclaration1747);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_TERM.add(TERM80);
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("structOrVariantDeclaration");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "structOrVariantDeclaration"

    public static class specifierQualifierList_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "specifierQualifierList"
    // Grammar/CTFParser.g:536:1: specifierQualifierList : ( typeQualifier |
    // typeSpecifier )+ -> ^( TYPE_SPECIFIER_LIST ( typeQualifier )* (
    // typeSpecifier )* ) ;
    public final TSDLParser.specifierQualifierList_return specifierQualifierList()
            throws RecognitionException {
        TSDLParser.specifierQualifierList_return retval = new TSDLParser.specifierQualifierList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.typeQualifier_return typeQualifier81 = null;

        TSDLParser.typeSpecifier_return typeSpecifier82 = null;

        RewriteRuleSubtreeStream stream_typeSpecifier = new RewriteRuleSubtreeStream(
                adaptor, "rule typeSpecifier");
        RewriteRuleSubtreeStream stream_typeQualifier = new RewriteRuleSubtreeStream(
                adaptor, "rule typeQualifier");

        enter("specifierQualifierList");

        try {
            // Grammar/CTFParser.g:543:1: ( ( typeQualifier | typeSpecifier )+
            // -> ^( TYPE_SPECIFIER_LIST ( typeQualifier )* ( typeSpecifier )* )
            // )
            // Grammar/CTFParser.g:544:3: ( typeQualifier | typeSpecifier )+
            {
                // Grammar/CTFParser.g:544:3: ( typeQualifier | typeSpecifier )+
                int cnt26 = 0;
                loop26: do {
                    int alt26 = 3;
                    int LA26_0 = input.LA(1);

                    if ((LA26_0 == CONSTTOK)) {
                        alt26 = 1;
                    } else if ((((LA26_0 >= CHARTOK) && (LA26_0 <= ENUMTOK))
                            || ((LA26_0 >= FLOATINGPOINTTOK) && (LA26_0 <= SIGNEDTOK))
                            || ((LA26_0 >= STRINGTOK) && (LA26_0 <= STRUCTTOK)) || ((LA26_0 >= UNSIGNEDTOK) && (LA26_0 <= IMAGINARYTOK)))) {
                        alt26 = 2;
                    } else if ((LA26_0 == IDENTIFIER)
                            && ((inTypealiasAlias() || isTypeName(input.LT(1)
                                    .getText())))) {
                        alt26 = 2;
                    }

                    switch (alt26) {
                    case 1:
                    // Grammar/CTFParser.g:544:4: typeQualifier
                    {
                        pushFollow(FOLLOW_typeQualifier_in_specifierQualifierList1771);
                        typeQualifier81 = typeQualifier();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_typeQualifier.add(typeQualifier81.getTree());
                        }

                    }
                        break;
                    case 2:
                    // Grammar/CTFParser.g:544:20: typeSpecifier
                    {
                        pushFollow(FOLLOW_typeSpecifier_in_specifierQualifierList1775);
                        typeSpecifier82 = typeSpecifier();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_typeSpecifier.add(typeSpecifier82.getTree());
                        }

                    }
                        break;

                    default:
                        if (cnt26 >= 1) {
                            break loop26;
                        }
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        EarlyExitException eee = new EarlyExitException(26,
                                input);
                        throw eee;
                    }
                    cnt26++;
                } while (true);

                // AST REWRITE
                // elements: typeSpecifier, typeQualifier
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 544:36: -> ^( TYPE_SPECIFIER_LIST ( typeQualifier )* (
                    // typeSpecifier )* )
                    {
                        // Grammar/CTFParser.g:544:39: ^( TYPE_SPECIFIER_LIST (
                        // typeQualifier )* ( typeSpecifier )* )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(TYPE_SPECIFIER_LIST,
                                            "TYPE_SPECIFIER_LIST"), root_1);

                            // Grammar/CTFParser.g:544:61: ( typeQualifier )*
                            while (stream_typeQualifier.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_typeQualifier.nextTree());

                            }
                            stream_typeQualifier.reset();
                            // Grammar/CTFParser.g:544:76: ( typeSpecifier )*
                            while (stream_typeSpecifier.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_typeSpecifier.nextTree());

                            }
                            stream_typeSpecifier.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("specifierQualifierList");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "specifierQualifierList"

    public static class structOrVariantDeclaratorList_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "structOrVariantDeclaratorList"
    // Grammar/CTFParser.g:547:1: structOrVariantDeclaratorList :
    // structOrVariantDeclarator ( SEPARATOR structOrVariantDeclarator )* -> ^(
    // TYPE_DECLARATOR_LIST ( structOrVariantDeclarator )+ ) ;
    public final TSDLParser.structOrVariantDeclaratorList_return structOrVariantDeclaratorList()
            throws RecognitionException {
        TSDLParser.structOrVariantDeclaratorList_return retval = new TSDLParser.structOrVariantDeclaratorList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEPARATOR84 = null;
        TSDLParser.structOrVariantDeclarator_return structOrVariantDeclarator83 = null;

        TSDLParser.structOrVariantDeclarator_return structOrVariantDeclarator85 = null;

        CommonTree SEPARATOR84_tree = null;
        RewriteRuleTokenStream stream_SEPARATOR = new RewriteRuleTokenStream(
                adaptor, "token SEPARATOR");
        RewriteRuleSubtreeStream stream_structOrVariantDeclarator = new RewriteRuleSubtreeStream(
                adaptor, "rule structOrVariantDeclarator");

        enter("structOrVariantDeclaratorList");

        try {
            // Grammar/CTFParser.g:554:1: ( structOrVariantDeclarator (
            // SEPARATOR structOrVariantDeclarator )* -> ^( TYPE_DECLARATOR_LIST
            // ( structOrVariantDeclarator )+ ) )
            // Grammar/CTFParser.g:555:3: structOrVariantDeclarator ( SEPARATOR
            // structOrVariantDeclarator )*
            {
                pushFollow(FOLLOW_structOrVariantDeclarator_in_structOrVariantDeclaratorList1812);
                structOrVariantDeclarator83 = structOrVariantDeclarator();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_structOrVariantDeclarator
                            .add(structOrVariantDeclarator83.getTree());
                }
                // Grammar/CTFParser.g:555:29: ( SEPARATOR
                // structOrVariantDeclarator )*
                loop27: do {
                    int alt27 = 2;
                    int LA27_0 = input.LA(1);

                    if ((LA27_0 == SEPARATOR)) {
                        alt27 = 1;
                    }

                    switch (alt27) {
                    case 1:
                    // Grammar/CTFParser.g:555:30: SEPARATOR
                    // structOrVariantDeclarator
                    {
                        SEPARATOR84 = (Token) match(input, SEPARATOR,
                                FOLLOW_SEPARATOR_in_structOrVariantDeclaratorList1815);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_SEPARATOR.add(SEPARATOR84);
                        }

                        pushFollow(FOLLOW_structOrVariantDeclarator_in_structOrVariantDeclaratorList1817);
                        structOrVariantDeclarator85 = structOrVariantDeclarator();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_structOrVariantDeclarator
                                    .add(structOrVariantDeclarator85.getTree());
                        }

                    }
                        break;

                    default:
                        break loop27;
                    }
                } while (true);

                // AST REWRITE
                // elements: structOrVariantDeclarator
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 555:68: -> ^( TYPE_DECLARATOR_LIST (
                    // structOrVariantDeclarator )+ )
                    {
                        // Grammar/CTFParser.g:555:71: ^( TYPE_DECLARATOR_LIST (
                        // structOrVariantDeclarator )+ )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(TYPE_DECLARATOR_LIST,
                                            "TYPE_DECLARATOR_LIST"), root_1);

                            if (!(stream_structOrVariantDeclarator.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_structOrVariantDeclarator.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_structOrVariantDeclarator
                                                .nextTree());

                            }
                            stream_structOrVariantDeclarator.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("structOrVariantDeclaratorList");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "structOrVariantDeclaratorList"

    public static class structOrVariantDeclarator_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "structOrVariantDeclarator"
    // Grammar/CTFParser.g:558:1: structOrVariantDeclarator : ( declarator (
    // COLON numberLiteral )? ) -> declarator ;
    public final TSDLParser.structOrVariantDeclarator_return structOrVariantDeclarator()
            throws RecognitionException {
        TSDLParser.structOrVariantDeclarator_return retval = new TSDLParser.structOrVariantDeclarator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COLON87 = null;
        TSDLParser.declarator_return declarator86 = null;

        TSDLParser.numberLiteral_return numberLiteral88 = null;

        CommonTree COLON87_tree = null;
        RewriteRuleTokenStream stream_COLON = new RewriteRuleTokenStream(
                adaptor, "token COLON");
        RewriteRuleSubtreeStream stream_declarator = new RewriteRuleSubtreeStream(
                adaptor, "rule declarator");
        RewriteRuleSubtreeStream stream_numberLiteral = new RewriteRuleSubtreeStream(
                adaptor, "rule numberLiteral");

        enter("structOrVariantDeclarator");

        try {
            // Grammar/CTFParser.g:565:1: ( ( declarator ( COLON numberLiteral
            // )? ) -> declarator )
            // Grammar/CTFParser.g:567:5: ( declarator ( COLON numberLiteral )?
            // )
            {
                // Grammar/CTFParser.g:567:5: ( declarator ( COLON numberLiteral
                // )? )
                // Grammar/CTFParser.g:567:6: declarator ( COLON numberLiteral
                // )?
                {
                    pushFollow(FOLLOW_declarator_in_structOrVariantDeclarator1858);
                    declarator86 = declarator();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_declarator.add(declarator86.getTree());
                    }
                    // Grammar/CTFParser.g:567:17: ( COLON numberLiteral )?
                    int alt28 = 2;
                    int LA28_0 = input.LA(1);

                    if ((LA28_0 == COLON)) {
                        alt28 = 1;
                    }
                    switch (alt28) {
                    case 1:
                    // Grammar/CTFParser.g:567:18: COLON numberLiteral
                    {
                        COLON87 = (Token) match(input, COLON,
                                FOLLOW_COLON_in_structOrVariantDeclarator1861);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_COLON.add(COLON87);
                        }

                        pushFollow(FOLLOW_numberLiteral_in_structOrVariantDeclarator1863);
                        numberLiteral88 = numberLiteral();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_numberLiteral.add(numberLiteral88.getTree());
                        }

                    }
                        break;

                    }

                }

                // AST REWRITE
                // elements: declarator
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 567:41: -> declarator
                    {
                        adaptor.addChild(root_0, stream_declarator.nextTree());

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("structOrVariantDeclarator");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "structOrVariantDeclarator"

    public static class variantSpecifier_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "variantSpecifier"
    // Grammar/CTFParser.g:571:1: variantSpecifier : VARIANTTOK ( ( variantName
    // ( ( variantTag ( variantBody | ) ) | variantBody ) ) | ( variantTag
    // variantBody ) | variantBody ) -> ^( VARIANT ( variantName )? ( variantTag
    // )? ( variantBody )? ) ;
    public final TSDLParser.variantSpecifier_return variantSpecifier()
            throws RecognitionException {
        TSDLParser.variantSpecifier_return retval = new TSDLParser.variantSpecifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token VARIANTTOK89 = null;
        TSDLParser.variantName_return variantName90 = null;

        TSDLParser.variantTag_return variantTag91 = null;

        TSDLParser.variantBody_return variantBody92 = null;

        TSDLParser.variantBody_return variantBody93 = null;

        TSDLParser.variantTag_return variantTag94 = null;

        TSDLParser.variantBody_return variantBody95 = null;

        TSDLParser.variantBody_return variantBody96 = null;

        CommonTree VARIANTTOK89_tree = null;
        RewriteRuleTokenStream stream_VARIANTTOK = new RewriteRuleTokenStream(
                adaptor, "token VARIANTTOK");
        RewriteRuleSubtreeStream stream_variantName = new RewriteRuleSubtreeStream(
                adaptor, "rule variantName");
        RewriteRuleSubtreeStream stream_variantTag = new RewriteRuleSubtreeStream(
                adaptor, "rule variantTag");
        RewriteRuleSubtreeStream stream_variantBody = new RewriteRuleSubtreeStream(
                adaptor, "rule variantBody");

        enter("variantSpecifier");

        try {
            // Grammar/CTFParser.g:578:1: ( VARIANTTOK ( ( variantName ( (
            // variantTag ( variantBody | ) ) | variantBody ) ) | ( variantTag
            // variantBody ) | variantBody ) -> ^( VARIANT ( variantName )? (
            // variantTag )? ( variantBody )? ) )
            // Grammar/CTFParser.g:579:3: VARIANTTOK ( ( variantName ( (
            // variantTag ( variantBody | ) ) | variantBody ) ) | ( variantTag
            // variantBody ) | variantBody )
            {
                VARIANTTOK89 = (Token) match(input, VARIANTTOK,
                        FOLLOW_VARIANTTOK_in_variantSpecifier1897);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_VARIANTTOK.add(VARIANTTOK89);
                }

                // Grammar/CTFParser.g:580:3: ( ( variantName ( ( variantTag (
                // variantBody | ) ) | variantBody ) ) | ( variantTag
                // variantBody ) | variantBody )
                int alt31 = 3;
                switch (input.LA(1)) {
                case IDENTIFIER: {
                    alt31 = 1;
                }
                    break;
                case LT: {
                    alt31 = 2;
                }
                    break;
                case LCURL: {
                    alt31 = 3;
                }
                    break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            31, 0, input);

                    throw nvae;
                }

                switch (alt31) {
                case 1:
                // Grammar/CTFParser.g:581:5: ( variantName ( ( variantTag (
                // variantBody | ) ) | variantBody ) )
                {
                    // Grammar/CTFParser.g:581:5: ( variantName ( ( variantTag (
                    // variantBody | ) ) | variantBody ) )
                    // Grammar/CTFParser.g:582:7: variantName ( ( variantTag (
                    // variantBody | ) ) | variantBody )
                    {
                        pushFollow(FOLLOW_variantName_in_variantSpecifier1915);
                        variantName90 = variantName();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_variantName.add(variantName90.getTree());
                        }
                        // Grammar/CTFParser.g:583:7: ( ( variantTag (
                        // variantBody | ) ) | variantBody )
                        int alt30 = 2;
                        int LA30_0 = input.LA(1);

                        if ((LA30_0 == LT)) {
                            alt30 = 1;
                        } else if ((LA30_0 == LCURL)) {
                            alt30 = 2;
                        } else {
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return retval;
                            }
                            NoViableAltException nvae = new NoViableAltException(
                                    "", 30, 0, input);

                            throw nvae;
                        }
                        switch (alt30) {
                        case 1:
                        // Grammar/CTFParser.g:584:9: ( variantTag ( variantBody
                        // | ) )
                        {
                            // Grammar/CTFParser.g:584:9: ( variantTag (
                            // variantBody | ) )
                            // Grammar/CTFParser.g:585:11: variantTag (
                            // variantBody | )
                            {
                                pushFollow(FOLLOW_variantTag_in_variantSpecifier1946);
                                variantTag91 = variantTag();

                                state._fsp--;
                                if (state.failed) {
                                    return retval;
                                }
                                if (state.backtracking == 0) {
                                    stream_variantTag.add(variantTag91
                                            .getTree());
                                }
                                // Grammar/CTFParser.g:586:11: ( variantBody | )
                                int alt29 = 2;
                                int LA29_0 = input.LA(1);

                                if ((LA29_0 == LCURL)) {
                                    switch (input.LA(2)) {
                                    case CONSTTOK:
                                    case CHARTOK:
                                    case DOUBLETOK:
                                    case ENUMTOK:
                                    case FLOATINGPOINTTOK:
                                    case FLOATTOK:
                                    case INTEGERTOK:
                                    case INTTOK:
                                    case LONGTOK:
                                    case SHORTTOK:
                                    case STRUCTTOK:
                                    case TYPEALIASTOK:
                                    case TYPEDEFTOK:
                                    case UNSIGNEDTOK:
                                    case VARIANTTOK:
                                    case VOIDTOK:
                                    case BOOLTOK:
                                    case COMPLEXTOK:
                                    case IMAGINARYTOK: {
                                        alt29 = 1;
                                    }
                                        break;
                                    case SIGNEDTOK: {
                                        int LA29_4 = input.LA(3);

                                        if ((((LA29_4 >= CONSTTOK) && (LA29_4 <= ENUMTOK))
                                                || ((LA29_4 >= FLOATINGPOINTTOK) && (LA29_4 <= SIGNEDTOK))
                                                || ((LA29_4 >= STRINGTOK) && (LA29_4 <= STRUCTTOK))
                                                || ((LA29_4 >= TYPEDEFTOK) && (LA29_4 <= IMAGINARYTOK))
                                                || (LA29_4 == POINTER) || (LA29_4 == IDENTIFIER))) {
                                            alt29 = 1;
                                        } else if (((LA29_4 == SEPARATOR)
                                                || (LA29_4 == ASSIGNMENT) || (LA29_4 == RCURL))) {
                                            alt29 = 2;
                                        } else {
                                            if (state.backtracking > 0) {
                                                state.failed = true;
                                                return retval;
                                            }
                                            NoViableAltException nvae = new NoViableAltException(
                                                    "", 29, 4, input);

                                            throw nvae;
                                        }
                                    }
                                        break;
                                    case STRINGTOK: {
                                        int LA29_5 = input.LA(3);

                                        if (((LA29_5 == SEPARATOR)
                                                || (LA29_5 == ASSIGNMENT) || (LA29_5 == RCURL))) {
                                            alt29 = 2;
                                        } else if ((((LA29_5 >= CONSTTOK) && (LA29_5 <= ENUMTOK))
                                                || ((LA29_5 >= FLOATINGPOINTTOK) && (LA29_5 <= SIGNEDTOK))
                                                || ((LA29_5 >= STRINGTOK) && (LA29_5 <= STRUCTTOK))
                                                || ((LA29_5 >= TYPEDEFTOK) && (LA29_5 <= IMAGINARYTOK))
                                                || (LA29_5 == LCURL)
                                                || (LA29_5 == POINTER) || (LA29_5 == IDENTIFIER))) {
                                            alt29 = 1;
                                        } else {
                                            if (state.backtracking > 0) {
                                                state.failed = true;
                                                return retval;
                                            }
                                            NoViableAltException nvae = new NoViableAltException(
                                                    "", 29, 5, input);

                                            throw nvae;
                                        }
                                    }
                                        break;
                                    case IDENTIFIER: {
                                        int LA29_6 = input.LA(3);

                                        if (((LA29_6 == SEPARATOR)
                                                || (LA29_6 == ASSIGNMENT) || (LA29_6 == RCURL))) {
                                            alt29 = 2;
                                        } else if ((((LA29_6 >= CONSTTOK) && (LA29_6 <= ENUMTOK))
                                                || ((LA29_6 >= FLOATINGPOINTTOK) && (LA29_6 <= SIGNEDTOK))
                                                || ((LA29_6 >= STRINGTOK) && (LA29_6 <= STRUCTTOK))
                                                || ((LA29_6 >= TYPEDEFTOK) && (LA29_6 <= IMAGINARYTOK))
                                                || (LA29_6 == POINTER) || (LA29_6 == IDENTIFIER))) {
                                            alt29 = 1;
                                        } else {
                                            if (state.backtracking > 0) {
                                                state.failed = true;
                                                return retval;
                                            }
                                            NoViableAltException nvae = new NoViableAltException(
                                                    "", 29, 6, input);

                                            throw nvae;
                                        }
                                    }
                                        break;
                                    case ALIGNTOK:
                                    case EVENTTOK:
                                    case STRING_LITERAL: {
                                        alt29 = 2;
                                    }
                                        break;
                                    default:
                                        if (state.backtracking > 0) {
                                            state.failed = true;
                                            return retval;
                                        }
                                        NoViableAltException nvae = new NoViableAltException(
                                                "", 29, 1, input);

                                        throw nvae;
                                    }

                                } else if (((LA29_0 == EOF)
                                        || ((LA29_0 >= CONSTTOK) && (LA29_0 <= ENUMTOK))
                                        || ((LA29_0 >= FLOATINGPOINTTOK) && (LA29_0 <= SIGNEDTOK))
                                        || ((LA29_0 >= STRINGTOK) && (LA29_0 <= STRUCTTOK))
                                        || ((LA29_0 >= TYPEDEFTOK) && (LA29_0 <= IMAGINARYTOK))
                                        || (LA29_0 == TYPE_ASSIGNMENT)
                                        || (LA29_0 == LPAREN)
                                        || ((LA29_0 >= TERM) && (LA29_0 <= POINTER)) || (LA29_0 == IDENTIFIER))) {
                                    alt29 = 2;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return retval;
                                    }
                                    NoViableAltException nvae = new NoViableAltException(
                                            "", 29, 0, input);

                                    throw nvae;
                                }
                                switch (alt29) {
                                case 1:
                                // Grammar/CTFParser.g:587:13: variantBody
                                {
                                    pushFollow(FOLLOW_variantBody_in_variantSpecifier1972);
                                    variantBody92 = variantBody();

                                    state._fsp--;
                                    if (state.failed) {
                                        return retval;
                                    }
                                    if (state.backtracking == 0) {
                                        stream_variantBody.add(variantBody92
                                                .getTree());
                                    }

                                }
                                    break;
                                case 2:
                                    break;

                                }

                            }

                        }
                            break;
                        case 2:
                        // Grammar/CTFParser.g:593:9: variantBody
                        {
                            pushFollow(FOLLOW_variantBody_in_variantSpecifier2040);
                            variantBody93 = variantBody();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_variantBody.add(variantBody93.getTree());
                            }

                        }
                            break;

                        }

                    }

                }
                    break;
                case 2:
                // Grammar/CTFParser.g:597:5: ( variantTag variantBody )
                {
                    // Grammar/CTFParser.g:597:5: ( variantTag variantBody )
                    // Grammar/CTFParser.g:597:6: variantTag variantBody
                    {
                        pushFollow(FOLLOW_variantTag_in_variantSpecifier2065);
                        variantTag94 = variantTag();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_variantTag.add(variantTag94.getTree());
                        }
                        pushFollow(FOLLOW_variantBody_in_variantSpecifier2067);
                        variantBody95 = variantBody();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_variantBody.add(variantBody95.getTree());
                        }

                    }

                }
                    break;
                case 3:
                // Grammar/CTFParser.g:599:5: variantBody
                {
                    pushFollow(FOLLOW_variantBody_in_variantSpecifier2078);
                    variantBody96 = variantBody();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_variantBody.add(variantBody96.getTree());
                    }

                }
                    break;

                }

                // AST REWRITE
                // elements: variantBody, variantTag, variantName
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 600:5: -> ^( VARIANT ( variantName )? ( variantTag )? (
                    // variantBody )? )
                    {
                        // Grammar/CTFParser.g:600:8: ^( VARIANT ( variantName
                        // )? ( variantTag )? ( variantBody )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(VARIANT, "VARIANT"), root_1);

                            // Grammar/CTFParser.g:600:18: ( variantName )?
                            if (stream_variantName.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_variantName.nextTree());

                            }
                            stream_variantName.reset();
                            // Grammar/CTFParser.g:600:31: ( variantTag )?
                            if (stream_variantTag.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_variantTag.nextTree());

                            }
                            stream_variantTag.reset();
                            // Grammar/CTFParser.g:600:43: ( variantBody )?
                            if (stream_variantBody.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_variantBody.nextTree());

                            }
                            stream_variantBody.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("variantSpecifier");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "variantSpecifier"

    public static class variantName_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "variantName"
    // Grammar/CTFParser.g:603:1: variantName : IDENTIFIER -> ^( VARIANT_NAME
    // IDENTIFIER ) ;
    public final TSDLParser.variantName_return variantName()
            throws RecognitionException {
        TSDLParser.variantName_return retval = new TSDLParser.variantName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENTIFIER97 = null;

        CommonTree IDENTIFIER97_tree = null;
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(
                adaptor, "token IDENTIFIER");

        enter("variantName");

        try {
            // Grammar/CTFParser.g:611:1: ( IDENTIFIER -> ^( VARIANT_NAME
            // IDENTIFIER ) )
            // Grammar/CTFParser.g:612:3: IDENTIFIER
            {
                IDENTIFIER97 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_variantName2120);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_IDENTIFIER.add(IDENTIFIER97);
                }

                // AST REWRITE
                // elements: IDENTIFIER
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 612:14: -> ^( VARIANT_NAME IDENTIFIER )
                    {
                        // Grammar/CTFParser.g:612:17: ^( VARIANT_NAME
                        // IDENTIFIER )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(VARIANT_NAME, "VARIANT_NAME"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_IDENTIFIER.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("variantName");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "variantName"

    public static class variantBody_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "variantBody"
    // Grammar/CTFParser.g:615:1: variantBody : LCURL
    // structOrVariantDeclarationList RCURL -> ^( VARIANT_BODY
    // structOrVariantDeclarationList ) ;
    public final TSDLParser.variantBody_return variantBody()
            throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        TSDLParser.variantBody_return retval = new TSDLParser.variantBody_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LCURL98 = null;
        Token RCURL100 = null;
        TSDLParser.structOrVariantDeclarationList_return structOrVariantDeclarationList99 = null;

        CommonTree LCURL98_tree = null;
        CommonTree RCURL100_tree = null;
        RewriteRuleTokenStream stream_LCURL = new RewriteRuleTokenStream(
                adaptor, "token LCURL");
        RewriteRuleTokenStream stream_RCURL = new RewriteRuleTokenStream(
                adaptor, "token RCURL");
        RewriteRuleSubtreeStream stream_structOrVariantDeclarationList = new RewriteRuleSubtreeStream(
                adaptor, "rule structOrVariantDeclarationList");

        enter("variantBody");
        debug_print("Scope push " + Symbols_stack.size());
        ((Symbols_scope) Symbols_stack.peek()).types = new HashSet<String>();

        try {
            // Grammar/CTFParser.g:626:1: ( LCURL structOrVariantDeclarationList
            // RCURL -> ^( VARIANT_BODY structOrVariantDeclarationList ) )
            // Grammar/CTFParser.g:627:3: LCURL structOrVariantDeclarationList
            // RCURL
            {
                LCURL98 = (Token) match(input, LCURL,
                        FOLLOW_LCURL_in_variantBody2156);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_LCURL.add(LCURL98);
                }

                pushFollow(FOLLOW_structOrVariantDeclarationList_in_variantBody2158);
                structOrVariantDeclarationList99 = structOrVariantDeclarationList();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_structOrVariantDeclarationList
                            .add(structOrVariantDeclarationList99.getTree());
                }
                RCURL100 = (Token) match(input, RCURL,
                        FOLLOW_RCURL_in_variantBody2160);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_RCURL.add(RCURL100);
                }

                // AST REWRITE
                // elements: structOrVariantDeclarationList
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 627:46: -> ^( VARIANT_BODY structOrVariantDeclarationList
                    // )
                    {
                        // Grammar/CTFParser.g:627:49: ^( VARIANT_BODY
                        // structOrVariantDeclarationList )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(VARIANT_BODY, "VARIANT_BODY"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_structOrVariantDeclarationList
                                            .nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print("Scope pop " + Symbols_stack.size());
                exit("variantBody");

            }
        }

        catch (RecognitionException e) {
            throw e;
        } finally {
            Symbols_stack.pop();

        }
        return retval;
    }

    // $ANTLR end "variantBody"

    public static class variantTag_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "variantTag"
    // Grammar/CTFParser.g:630:1: variantTag : LT IDENTIFIER GT -> ^(
    // VARIANT_TAG IDENTIFIER ) ;
    public final TSDLParser.variantTag_return variantTag()
            throws RecognitionException {
        TSDLParser.variantTag_return retval = new TSDLParser.variantTag_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LT101 = null;
        Token IDENTIFIER102 = null;
        Token GT103 = null;

        CommonTree LT101_tree = null;
        CommonTree IDENTIFIER102_tree = null;
        CommonTree GT103_tree = null;
        RewriteRuleTokenStream stream_GT = new RewriteRuleTokenStream(adaptor,
                "token GT");
        RewriteRuleTokenStream stream_LT = new RewriteRuleTokenStream(adaptor,
                "token LT");
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(
                adaptor, "token IDENTIFIER");

        enter("variantTag");

        try {
            // Grammar/CTFParser.g:638:1: ( LT IDENTIFIER GT -> ^( VARIANT_TAG
            // IDENTIFIER ) )
            // Grammar/CTFParser.g:639:3: LT IDENTIFIER GT
            {
                LT101 = (Token) match(input, LT, FOLLOW_LT_in_variantTag2191);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_LT.add(LT101);
                }

                IDENTIFIER102 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_variantTag2193);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_IDENTIFIER.add(IDENTIFIER102);
                }

                GT103 = (Token) match(input, GT, FOLLOW_GT_in_variantTag2195);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_GT.add(GT103);
                }

                // AST REWRITE
                // elements: IDENTIFIER
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 639:20: -> ^( VARIANT_TAG IDENTIFIER )
                    {
                        // Grammar/CTFParser.g:639:23: ^( VARIANT_TAG IDENTIFIER
                        // )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(VARIANT_TAG, "VARIANT_TAG"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_IDENTIFIER.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("variantTag");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "variantTag"

    public static class enumSpecifier_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumSpecifier"
    // Grammar/CTFParser.g:642:1: enumSpecifier : ENUMTOK ( ( enumName (
    // enumContainerType enumBody | enumBody | ) ) | ( enumContainerType
    // enumBody | enumBody ) ) -> ^( ENUM ( enumName )? ( enumContainerType )? (
    // enumBody )? ) ;
    public final TSDLParser.enumSpecifier_return enumSpecifier()
            throws RecognitionException {
        TSDLParser.enumSpecifier_return retval = new TSDLParser.enumSpecifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ENUMTOK104 = null;
        TSDLParser.enumName_return enumName105 = null;

        TSDLParser.enumContainerType_return enumContainerType106 = null;

        TSDLParser.enumBody_return enumBody107 = null;

        TSDLParser.enumBody_return enumBody108 = null;

        TSDLParser.enumContainerType_return enumContainerType109 = null;

        TSDLParser.enumBody_return enumBody110 = null;

        TSDLParser.enumBody_return enumBody111 = null;

        CommonTree ENUMTOK104_tree = null;
        RewriteRuleTokenStream stream_ENUMTOK = new RewriteRuleTokenStream(
                adaptor, "token ENUMTOK");
        RewriteRuleSubtreeStream stream_enumName = new RewriteRuleSubtreeStream(
                adaptor, "rule enumName");
        RewriteRuleSubtreeStream stream_enumContainerType = new RewriteRuleSubtreeStream(
                adaptor, "rule enumContainerType");
        RewriteRuleSubtreeStream stream_enumBody = new RewriteRuleSubtreeStream(
                adaptor, "rule enumBody");

        enter("enumSpecifier");

        try {
            // Grammar/CTFParser.g:649:1: ( ENUMTOK ( ( enumName (
            // enumContainerType enumBody | enumBody | ) ) | ( enumContainerType
            // enumBody | enumBody ) ) -> ^( ENUM ( enumName )? (
            // enumContainerType )? ( enumBody )? ) )
            // Grammar/CTFParser.g:650:2: ENUMTOK ( ( enumName (
            // enumContainerType enumBody | enumBody | ) ) | ( enumContainerType
            // enumBody | enumBody ) )
            {
                ENUMTOK104 = (Token) match(input, ENUMTOK,
                        FOLLOW_ENUMTOK_in_enumSpecifier2225);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ENUMTOK.add(ENUMTOK104);
                }

                // Grammar/CTFParser.g:651:2: ( ( enumName ( enumContainerType
                // enumBody | enumBody | ) ) | ( enumContainerType enumBody |
                // enumBody ) )
                int alt34 = 2;
                int LA34_0 = input.LA(1);

                if ((LA34_0 == IDENTIFIER)) {
                    alt34 = 1;
                } else if (((LA34_0 == COLON) || (LA34_0 == LCURL))) {
                    alt34 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            34, 0, input);

                    throw nvae;
                }
                switch (alt34) {
                case 1:
                // Grammar/CTFParser.g:653:3: ( enumName ( enumContainerType
                // enumBody | enumBody | ) )
                {
                    // Grammar/CTFParser.g:653:3: ( enumName ( enumContainerType
                    // enumBody | enumBody | ) )
                    // Grammar/CTFParser.g:654:4: enumName ( enumContainerType
                    // enumBody | enumBody | )
                    {
                        pushFollow(FOLLOW_enumName_in_enumSpecifier2240);
                        enumName105 = enumName();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_enumName.add(enumName105.getTree());
                        }
                        // Grammar/CTFParser.g:655:4: ( enumContainerType
                        // enumBody | enumBody | )
                        int alt32 = 3;
                        alt32 = dfa32.predict(input);
                        switch (alt32) {
                        case 1:
                        // Grammar/CTFParser.g:656:5: enumContainerType enumBody
                        {
                            pushFollow(FOLLOW_enumContainerType_in_enumSpecifier2251);
                            enumContainerType106 = enumContainerType();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_enumContainerType
                                        .add(enumContainerType106.getTree());
                            }
                            pushFollow(FOLLOW_enumBody_in_enumSpecifier2253);
                            enumBody107 = enumBody();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_enumBody.add(enumBody107.getTree());
                            }

                        }
                            break;
                        case 2:
                        // Grammar/CTFParser.g:658:5: enumBody
                        {
                            pushFollow(FOLLOW_enumBody_in_enumSpecifier2265);
                            enumBody108 = enumBody();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_enumBody.add(enumBody108.getTree());
                            }

                        }
                            break;
                        case 3:
                            break;
                        }
                    }
                }
                    break;
                case 2:
                // Grammar/CTFParser.g:665:3: ( enumContainerType enumBody |
                // enumBody )
                {
                    // Grammar/CTFParser.g:665:3: ( enumContainerType enumBody |
                    // enumBody )
                    int alt33 = 2;
                    int LA33_0 = input.LA(1);

                    if ((LA33_0 == COLON)) {
                        alt33 = 1;
                    } else if ((LA33_0 == LCURL)) {
                        alt33 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae = new NoViableAltException(
                                "", 33, 0, input);

                        throw nvae;
                    }
                    switch (alt33) {
                    case 1:
                    // Grammar/CTFParser.g:666:4: enumContainerType enumBody
                    {
                        pushFollow(FOLLOW_enumContainerType_in_enumSpecifier2300);
                        enumContainerType109 = enumContainerType();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_enumContainerType.add(enumContainerType109
                                    .getTree());
                        }
                        pushFollow(FOLLOW_enumBody_in_enumSpecifier2302);
                        enumBody110 = enumBody();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_enumBody.add(enumBody110.getTree());
                        }

                    }
                        break;
                    case 2:
                    // Grammar/CTFParser.g:668:4: enumBody
                    {
                        pushFollow(FOLLOW_enumBody_in_enumSpecifier2311);
                        enumBody111 = enumBody();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_enumBody.add(enumBody111.getTree());
                        }

                    }
                        break;

                    }

                }
                    break;

                }

                // AST REWRITE
                // elements: enumContainerType, enumName, enumBody
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 670:4: -> ^( ENUM ( enumName )? ( enumContainerType )? (
                    // enumBody )? )
                    {
                        // Grammar/CTFParser.g:670:7: ^( ENUM ( enumName )? (
                        // enumContainerType )? ( enumBody )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(ENUM, "ENUM"), root_1);

                            // Grammar/CTFParser.g:670:14: ( enumName )?
                            if (stream_enumName.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_enumName.nextTree());

                            }
                            stream_enumName.reset();
                            // Grammar/CTFParser.g:670:24: ( enumContainerType
                            // )?
                            if (stream_enumContainerType.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_enumContainerType.nextTree());

                            }
                            stream_enumContainerType.reset();
                            // Grammar/CTFParser.g:670:43: ( enumBody )?
                            if (stream_enumBody.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_enumBody.nextTree());

                            }
                            stream_enumBody.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("enumSpecifier");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "enumSpecifier"

    public static class enumName_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumName"
    // Grammar/CTFParser.g:673:1: enumName : IDENTIFIER -> ^( ENUM_NAME
    // IDENTIFIER ) ;
    public final TSDLParser.enumName_return enumName()
            throws RecognitionException {
        TSDLParser.enumName_return retval = new TSDLParser.enumName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENTIFIER112 = null;

        CommonTree IDENTIFIER112_tree = null;
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(
                adaptor, "token IDENTIFIER");

        enter("enumName");

        try {
            // Grammar/CTFParser.g:681:1: ( IDENTIFIER -> ^( ENUM_NAME
            // IDENTIFIER ) )
            // Grammar/CTFParser.g:682:3: IDENTIFIER
            {
                IDENTIFIER112 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_enumName2356);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_IDENTIFIER.add(IDENTIFIER112);
                }

                // AST REWRITE
                // elements: IDENTIFIER
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 682:14: -> ^( ENUM_NAME IDENTIFIER )
                    {
                        // Grammar/CTFParser.g:682:17: ^( ENUM_NAME IDENTIFIER )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(ENUM_NAME, "ENUM_NAME"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_IDENTIFIER.nextNode());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("enumName");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "enumName"

    public static class enumBody_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumBody"
    // Grammar/CTFParser.g:685:1: enumBody : LCURL enumeratorList ( SEPARATOR
    // RCURL | RCURL ) -> ^( ENUM_BODY enumeratorList ) ;
    public final TSDLParser.enumBody_return enumBody()
            throws RecognitionException {
        TSDLParser.enumBody_return retval = new TSDLParser.enumBody_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LCURL113 = null;
        Token SEPARATOR115 = null;
        Token RCURL116 = null;
        Token RCURL117 = null;
        TSDLParser.enumeratorList_return enumeratorList114 = null;

        CommonTree LCURL113_tree = null;
        CommonTree SEPARATOR115_tree = null;
        CommonTree RCURL116_tree = null;
        CommonTree RCURL117_tree = null;
        RewriteRuleTokenStream stream_LCURL = new RewriteRuleTokenStream(
                adaptor, "token LCURL");
        RewriteRuleTokenStream stream_SEPARATOR = new RewriteRuleTokenStream(
                adaptor, "token SEPARATOR");
        RewriteRuleTokenStream stream_RCURL = new RewriteRuleTokenStream(
                adaptor, "token RCURL");
        RewriteRuleSubtreeStream stream_enumeratorList = new RewriteRuleSubtreeStream(
                adaptor, "rule enumeratorList");

        enter("enumBody");

        try {
            // Grammar/CTFParser.g:692:1: ( LCURL enumeratorList ( SEPARATOR
            // RCURL | RCURL ) -> ^( ENUM_BODY enumeratorList ) )
            // Grammar/CTFParser.g:693:3: LCURL enumeratorList ( SEPARATOR RCURL
            // | RCURL )
            {
                LCURL113 = (Token) match(input, LCURL,
                        FOLLOW_LCURL_in_enumBody2389);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_LCURL.add(LCURL113);
                }

                pushFollow(FOLLOW_enumeratorList_in_enumBody2391);
                enumeratorList114 = enumeratorList();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_enumeratorList.add(enumeratorList114.getTree());
                }
                // Grammar/CTFParser.g:693:24: ( SEPARATOR RCURL | RCURL )
                int alt35 = 2;
                int LA35_0 = input.LA(1);

                if ((LA35_0 == SEPARATOR)) {
                    alt35 = 1;
                } else if ((LA35_0 == RCURL)) {
                    alt35 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            35, 0, input);

                    throw nvae;
                }
                switch (alt35) {
                case 1:
                // Grammar/CTFParser.g:693:25: SEPARATOR RCURL
                {
                    SEPARATOR115 = (Token) match(input, SEPARATOR,
                            FOLLOW_SEPARATOR_in_enumBody2394);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_SEPARATOR.add(SEPARATOR115);
                    }

                    RCURL116 = (Token) match(input, RCURL,
                            FOLLOW_RCURL_in_enumBody2396);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_RCURL.add(RCURL116);
                    }

                }
                    break;
                case 2:
                // Grammar/CTFParser.g:693:43: RCURL
                {
                    RCURL117 = (Token) match(input, RCURL,
                            FOLLOW_RCURL_in_enumBody2400);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_RCURL.add(RCURL117);
                    }

                }
                    break;

                }

                // AST REWRITE
                // elements: enumeratorList
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 693:50: -> ^( ENUM_BODY enumeratorList )
                    {
                        // Grammar/CTFParser.g:693:53: ^( ENUM_BODY
                        // enumeratorList )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(ENUM_BODY, "ENUM_BODY"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_enumeratorList.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("enumBody");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "enumBody"

    public static class enumContainerType_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumContainerType"
    // Grammar/CTFParser.g:696:1: enumContainerType : COLON
    // declarationSpecifiers -> ^( ENUM_CONTAINER_TYPE declarationSpecifiers ) ;
    public final TSDLParser.enumContainerType_return enumContainerType()
            throws RecognitionException {
        TSDLParser.enumContainerType_return retval = new TSDLParser.enumContainerType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COLON118 = null;
        TSDLParser.declarationSpecifiers_return declarationSpecifiers119 = null;

        CommonTree COLON118_tree = null;
        RewriteRuleTokenStream stream_COLON = new RewriteRuleTokenStream(
                adaptor, "token COLON");
        RewriteRuleSubtreeStream stream_declarationSpecifiers = new RewriteRuleSubtreeStream(
                adaptor, "rule declarationSpecifiers");

        enter("enumContainerType");

        try {
            // Grammar/CTFParser.g:703:1: ( COLON declarationSpecifiers -> ^(
            // ENUM_CONTAINER_TYPE declarationSpecifiers ) )
            // Grammar/CTFParser.g:704:3: COLON declarationSpecifiers
            {
                COLON118 = (Token) match(input, COLON,
                        FOLLOW_COLON_in_enumContainerType2432);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_COLON.add(COLON118);
                }

                pushFollow(FOLLOW_declarationSpecifiers_in_enumContainerType2434);
                declarationSpecifiers119 = declarationSpecifiers();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_declarationSpecifiers.add(declarationSpecifiers119
                            .getTree());
                }

                // AST REWRITE
                // elements: declarationSpecifiers
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 704:31: -> ^( ENUM_CONTAINER_TYPE declarationSpecifiers )
                    {
                        // Grammar/CTFParser.g:704:34: ^( ENUM_CONTAINER_TYPE
                        // declarationSpecifiers )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(ENUM_CONTAINER_TYPE,
                                            "ENUM_CONTAINER_TYPE"), root_1);

                            adaptor.addChild(root_1,
                                    stream_declarationSpecifiers.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("enumContainerType");

            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        return retval;
    }

    // $ANTLR end "enumContainerType"

    public static class enumeratorList_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumeratorList"
    // Grammar/CTFParser.g:707:1: enumeratorList : enumerator ( SEPARATOR
    // enumerator )* -> ( ^( ENUM_ENUMERATOR enumerator ) )+ ;
    public final TSDLParser.enumeratorList_return enumeratorList()
            throws RecognitionException {
        TSDLParser.enumeratorList_return retval = new TSDLParser.enumeratorList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEPARATOR121 = null;
        TSDLParser.enumerator_return enumerator120 = null;

        TSDLParser.enumerator_return enumerator122 = null;

        CommonTree SEPARATOR121_tree = null;
        RewriteRuleTokenStream stream_SEPARATOR = new RewriteRuleTokenStream(
                adaptor, "token SEPARATOR");
        RewriteRuleSubtreeStream stream_enumerator = new RewriteRuleSubtreeStream(
                adaptor, "rule enumerator");

        enter("enumeratorList");

        try {
            // Grammar/CTFParser.g:714:1: ( enumerator ( SEPARATOR enumerator )*
            // -> ( ^( ENUM_ENUMERATOR enumerator ) )+ )
            // Grammar/CTFParser.g:715:3: enumerator ( SEPARATOR enumerator )*
            {
                pushFollow(FOLLOW_enumerator_in_enumeratorList2465);
                enumerator120 = enumerator();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_enumerator.add(enumerator120.getTree());
                }
                // Grammar/CTFParser.g:715:14: ( SEPARATOR enumerator )*
                loop36: do {
                    int alt36 = 2;
                    int LA36_0 = input.LA(1);

                    if ((LA36_0 == SEPARATOR)) {
                        int LA36_1 = input.LA(2);

                        if (((LA36_1 == ALIGNTOK) || (LA36_1 == EVENTTOK)
                                || (LA36_1 == SIGNEDTOK)
                                || (LA36_1 == STRINGTOK)
                                || (LA36_1 == STRING_LITERAL) || (LA36_1 == IDENTIFIER))) {
                            alt36 = 1;
                        }

                    }

                    switch (alt36) {
                    case 1:
                    // Grammar/CTFParser.g:715:15: SEPARATOR enumerator
                    {
                        SEPARATOR121 = (Token) match(input, SEPARATOR,
                                FOLLOW_SEPARATOR_in_enumeratorList2468);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_SEPARATOR.add(SEPARATOR121);
                        }

                        pushFollow(FOLLOW_enumerator_in_enumeratorList2470);
                        enumerator122 = enumerator();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_enumerator.add(enumerator122.getTree());
                        }

                    }
                        break;

                    default:
                        break loop36;
                    }
                } while (true);

                // AST REWRITE
                // elements: enumerator
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 715:38: -> ( ^( ENUM_ENUMERATOR enumerator ) )+
                    {
                        if (!(stream_enumerator.hasNext())) {
                            throw new RewriteEarlyExitException();
                        }
                        while (stream_enumerator.hasNext()) {
                            // Grammar/CTFParser.g:715:42: ^( ENUM_ENUMERATOR
                            // enumerator )
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(
                                        adaptor.create(ENUM_ENUMERATOR,
                                                "ENUM_ENUMERATOR"), root_1);

                                adaptor.addChild(root_1,
                                        stream_enumerator.nextTree());

                                adaptor.addChild(root_0, root_1);
                            }

                        }
                        stream_enumerator.reset();

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("enumeratorList");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "enumeratorList"

    public static class enumerator_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumerator"
    // Grammar/CTFParser.g:718:1: enumerator : enumConstant ( enumeratorValue )?
    // ;
    public final TSDLParser.enumerator_return enumerator()
            throws RecognitionException {
        TSDLParser.enumerator_return retval = new TSDLParser.enumerator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.enumConstant_return enumConstant123 = null;

        TSDLParser.enumeratorValue_return enumeratorValue124 = null;

        enter("enumerator");

        try {
            // Grammar/CTFParser.g:725:1: ( enumConstant ( enumeratorValue )? )
            // Grammar/CTFParser.g:726:3: enumConstant ( enumeratorValue )?
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_enumConstant_in_enumerator2506);
                enumConstant123 = enumConstant();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, enumConstant123.getTree());
                }
                // Grammar/CTFParser.g:726:16: ( enumeratorValue )?
                int alt37 = 2;
                int LA37_0 = input.LA(1);

                if ((LA37_0 == ASSIGNMENT)) {
                    alt37 = 1;
                }
                switch (alt37) {
                case 1:
                // Grammar/CTFParser.g:726:16: enumeratorValue
                {
                    pushFollow(FOLLOW_enumeratorValue_in_enumerator2508);
                    enumeratorValue124 = enumeratorValue();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        adaptor.addChild(root_0, enumeratorValue124.getTree());
                    }

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("enumerator");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "enumerator"

    public static class enumeratorValue_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "enumeratorValue"
    // Grammar/CTFParser.g:729:1: enumeratorValue : ASSIGNMENT e1=
    // unaryExpression ( -> ^( ENUM_VALUE $e1) | ELIPSES e2= unaryExpression ->
    // ^( ENUM_VALUE_RANGE $e1 $e2) ) ;
    public final TSDLParser.enumeratorValue_return enumeratorValue()
            throws RecognitionException {
        TSDLParser.enumeratorValue_return retval = new TSDLParser.enumeratorValue_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ASSIGNMENT125 = null;
        Token ELIPSES126 = null;
        TSDLParser.unaryExpression_return e1 = null;

        TSDLParser.unaryExpression_return e2 = null;

        CommonTree ASSIGNMENT125_tree = null;
        CommonTree ELIPSES126_tree = null;
        RewriteRuleTokenStream stream_ASSIGNMENT = new RewriteRuleTokenStream(
                adaptor, "token ASSIGNMENT");
        RewriteRuleTokenStream stream_ELIPSES = new RewriteRuleTokenStream(
                adaptor, "token ELIPSES");
        RewriteRuleSubtreeStream stream_unaryExpression = new RewriteRuleSubtreeStream(
                adaptor, "rule unaryExpression");

        enter("enumeratorValue");

        try {
            // Grammar/CTFParser.g:736:1: ( ASSIGNMENT e1= unaryExpression ( ->
            // ^( ENUM_VALUE $e1) | ELIPSES e2= unaryExpression -> ^(
            // ENUM_VALUE_RANGE $e1 $e2) ) )
            // Grammar/CTFParser.g:737:3: ASSIGNMENT e1= unaryExpression ( -> ^(
            // ENUM_VALUE $e1) | ELIPSES e2= unaryExpression -> ^(
            // ENUM_VALUE_RANGE $e1 $e2) )
            {
                ASSIGNMENT125 = (Token) match(input, ASSIGNMENT,
                        FOLLOW_ASSIGNMENT_in_enumeratorValue2532);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ASSIGNMENT.add(ASSIGNMENT125);
                }

                pushFollow(FOLLOW_unaryExpression_in_enumeratorValue2536);
                e1 = unaryExpression();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_unaryExpression.add(e1.getTree());
                }
                // Grammar/CTFParser.g:738:3: ( -> ^( ENUM_VALUE $e1) | ELIPSES
                // e2= unaryExpression -> ^( ENUM_VALUE_RANGE $e1 $e2) )
                int alt38 = 2;
                int LA38_0 = input.LA(1);

                if (((LA38_0 == SEPARATOR) || (LA38_0 == RCURL))) {
                    alt38 = 1;
                } else if ((LA38_0 == ELIPSES)) {
                    alt38 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            38, 0, input);

                    throw nvae;
                }
                switch (alt38) {
                case 1:
                // Grammar/CTFParser.g:739:5:
                {

                    // AST REWRITE
                    // elements: e1
                    // token labels:
                    // rule labels: retval, e1
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if (state.backtracking == 0) {
                        retval.tree = root_0;
//                        RewriteRuleSubtreeStream stream_retval =
                                new RewriteRuleSubtreeStream(
                                adaptor, "rule retval",
                                retval != null ? retval.tree : null);
                        RewriteRuleSubtreeStream stream_e1 = new RewriteRuleSubtreeStream(
                                adaptor, "rule e1", e1 != null ? e1.tree : null);

                        root_0 = (CommonTree) adaptor.nil();
                        // 739:5: -> ^( ENUM_VALUE $e1)
                        {
                            // Grammar/CTFParser.g:739:8: ^( ENUM_VALUE $e1)
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor
                                        .becomeRoot(adaptor.create(ENUM_VALUE,
                                                "ENUM_VALUE"), root_1);

                                adaptor.addChild(root_1, stream_e1.nextTree());

                                adaptor.addChild(root_0, root_1);
                            }

                        }

                        retval.tree = root_0;
                    }
                }
                    break;
                case 2:
                // Grammar/CTFParser.g:740:7: ELIPSES e2= unaryExpression
                {
                    ELIPSES126 = (Token) match(input, ELIPSES,
                            FOLLOW_ELIPSES_in_enumeratorValue2562);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_ELIPSES.add(ELIPSES126);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_enumeratorValue2566);
                    e2 = unaryExpression();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_unaryExpression.add(e2.getTree());
                    }

                    // AST REWRITE
                    // elements: e2, e1
                    // token labels:
                    // rule labels: retval, e1, e2
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if (state.backtracking == 0) {
                        retval.tree = root_0;
//                        RewriteRuleSubtreeStream stream_retval =
                                new RewriteRuleSubtreeStream(
                                adaptor, "rule retval",
                                retval != null ? retval.tree : null);
                        RewriteRuleSubtreeStream stream_e1 = new RewriteRuleSubtreeStream(
                                adaptor, "rule e1", e1 != null ? e1.tree : null);
                        RewriteRuleSubtreeStream stream_e2 = new RewriteRuleSubtreeStream(
                                adaptor, "rule e2", e2 != null ? e2.tree : null);

                        root_0 = (CommonTree) adaptor.nil();
                        // 740:34: -> ^( ENUM_VALUE_RANGE $e1 $e2)
                        {
                            // Grammar/CTFParser.g:740:37: ^( ENUM_VALUE_RANGE
                            // $e1 $e2)
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(
                                        adaptor.create(ENUM_VALUE_RANGE,
                                                "ENUM_VALUE_RANGE"), root_1);

                                adaptor.addChild(root_1, stream_e1.nextTree());
                                adaptor.addChild(root_1, stream_e2.nextTree());

                                adaptor.addChild(root_0, root_1);
                            }

                        }

                        retval.tree = root_0;
                    }
                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("enumeratorValue");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "enumeratorValue"

    public static class declarator_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "declarator"
    // Grammar/CTFParser.g:745:1: declarator : ( pointer )* directDeclarator ->
    // ^( TYPE_DECLARATOR ( pointer )* directDeclarator ) ;
    public final TSDLParser.declarator_return declarator()
            throws RecognitionException {
        TSDLParser.declarator_return retval = new TSDLParser.declarator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.pointer_return pointer127 = null;

        TSDLParser.directDeclarator_return directDeclarator128 = null;

        RewriteRuleSubtreeStream stream_directDeclarator = new RewriteRuleSubtreeStream(
                adaptor, "rule directDeclarator");
        RewriteRuleSubtreeStream stream_pointer = new RewriteRuleSubtreeStream(
                adaptor, "rule pointer");

        enter("declarator");

        try {
            // Grammar/CTFParser.g:752:1: ( ( pointer )* directDeclarator -> ^(
            // TYPE_DECLARATOR ( pointer )* directDeclarator ) )
            // Grammar/CTFParser.g:753:3: ( pointer )* directDeclarator
            {
                // Grammar/CTFParser.g:753:3: ( pointer )*
                loop39: do {
                    int alt39 = 2;
                    int LA39_0 = input.LA(1);

                    if ((LA39_0 == POINTER)) {
                        alt39 = 1;
                    }

                    switch (alt39) {
                    case 1:
                    // Grammar/CTFParser.g:753:3: pointer
                    {
                        pushFollow(FOLLOW_pointer_in_declarator2608);
                        pointer127 = pointer();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_pointer.add(pointer127.getTree());
                        }

                    }
                        break;

                    default:
                        break loop39;
                    }
                } while (true);

                pushFollow(FOLLOW_directDeclarator_in_declarator2611);
                directDeclarator128 = directDeclarator();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_directDeclarator.add(directDeclarator128.getTree());
                }

                // AST REWRITE
                // elements: directDeclarator, pointer
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 753:29: -> ^( TYPE_DECLARATOR ( pointer )*
                    // directDeclarator )
                    {
                        // Grammar/CTFParser.g:753:32: ^( TYPE_DECLARATOR (
                        // pointer )* directDeclarator )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(TYPE_DECLARATOR,
                                            "TYPE_DECLARATOR"), root_1);

                            // Grammar/CTFParser.g:753:50: ( pointer )*
                            while (stream_pointer.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_pointer.nextTree());

                            }
                            stream_pointer.reset();
                            adaptor.addChild(root_1,
                                    stream_directDeclarator.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("declarator");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "declarator"

    public static class directDeclarator_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "directDeclarator"
    // Grammar/CTFParser.g:756:1: directDeclarator : ( IDENTIFIER ) (
    // directDeclaratorSuffix )* ;
    public final TSDLParser.directDeclarator_return directDeclarator()
            throws RecognitionException {
        TSDLParser.directDeclarator_return retval = new TSDLParser.directDeclarator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENTIFIER129 = null;
        TSDLParser.directDeclaratorSuffix_return directDeclaratorSuffix130 = null;

        CommonTree IDENTIFIER129_tree = null;

        enter("directDeclarator");

        try {
            // Grammar/CTFParser.g:763:1: ( ( IDENTIFIER ) (
            // directDeclaratorSuffix )* )
            // Grammar/CTFParser.g:764:3: ( IDENTIFIER ) (
            // directDeclaratorSuffix )*
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:764:3: ( IDENTIFIER )
                // Grammar/CTFParser.g:765:6: IDENTIFIER
                {
                    IDENTIFIER129 = (Token) match(input, IDENTIFIER,
                            FOLLOW_IDENTIFIER_in_directDeclarator2655);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        IDENTIFIER129_tree = (CommonTree) adaptor
                                .create(IDENTIFIER129);
                        adaptor.addChild(root_0, IDENTIFIER129_tree);
                    }
                    if (state.backtracking == 0) {
                        if (inTypedef()) {
                            addTypeName((IDENTIFIER129 != null ? IDENTIFIER129
                                    .getText() : null));
                        }
                    }
                    if (state.backtracking == 0) {
                        debug_print((IDENTIFIER129 != null ? IDENTIFIER129
                                .getText() : null));
                    }

                }

                // Grammar/CTFParser.g:768:2: ( directDeclaratorSuffix )*
                loop40: do {
                    int alt40 = 2;
                    int LA40_0 = input.LA(1);

                    if ((LA40_0 == OPENBRAC)) {
                        alt40 = 1;
                    }

                    switch (alt40) {
                    case 1:
                    // Grammar/CTFParser.g:768:2: directDeclaratorSuffix
                    {
                        pushFollow(FOLLOW_directDeclaratorSuffix_in_directDeclarator2673);
                        directDeclaratorSuffix130 = directDeclaratorSuffix();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    directDeclaratorSuffix130.getTree());
                        }

                    }
                        break;

                    default:
                        break loop40;
                    }
                } while (true);

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("directDeclarator");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "directDeclarator"

    public static class directDeclaratorSuffix_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "directDeclaratorSuffix"
    // Grammar/CTFParser.g:771:1: directDeclaratorSuffix : OPENBRAC
    // directDeclaratorLength CLOSEBRAC -> ^( LENGTH directDeclaratorLength ) ;
    public final TSDLParser.directDeclaratorSuffix_return directDeclaratorSuffix()
            throws RecognitionException {
        TSDLParser.directDeclaratorSuffix_return retval = new TSDLParser.directDeclaratorSuffix_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token OPENBRAC131 = null;
        Token CLOSEBRAC133 = null;
        TSDLParser.directDeclaratorLength_return directDeclaratorLength132 = null;

        CommonTree OPENBRAC131_tree = null;
        CommonTree CLOSEBRAC133_tree = null;
        RewriteRuleTokenStream stream_OPENBRAC = new RewriteRuleTokenStream(
                adaptor, "token OPENBRAC");
        RewriteRuleTokenStream stream_CLOSEBRAC = new RewriteRuleTokenStream(
                adaptor, "token CLOSEBRAC");
        RewriteRuleSubtreeStream stream_directDeclaratorLength = new RewriteRuleSubtreeStream(
                adaptor, "rule directDeclaratorLength");
        try {
            // Grammar/CTFParser.g:771:23: ( OPENBRAC directDeclaratorLength
            // CLOSEBRAC -> ^( LENGTH directDeclaratorLength ) )
            // Grammar/CTFParser.g:772:3: OPENBRAC directDeclaratorLength
            // CLOSEBRAC
            {
                OPENBRAC131 = (Token) match(input, OPENBRAC,
                        FOLLOW_OPENBRAC_in_directDeclaratorSuffix2686);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_OPENBRAC.add(OPENBRAC131);
                }

                pushFollow(FOLLOW_directDeclaratorLength_in_directDeclaratorSuffix2688);
                directDeclaratorLength132 = directDeclaratorLength();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_directDeclaratorLength.add(directDeclaratorLength132
                            .getTree());
                }
                CLOSEBRAC133 = (Token) match(input, CLOSEBRAC,
                        FOLLOW_CLOSEBRAC_in_directDeclaratorSuffix2690);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_CLOSEBRAC.add(CLOSEBRAC133);
                }

                // AST REWRITE
                // elements: directDeclaratorLength
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 772:45: -> ^( LENGTH directDeclaratorLength )
                    {
                        // Grammar/CTFParser.g:772:48: ^( LENGTH
                        // directDeclaratorLength )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(LENGTH, "LENGTH"), root_1);

                            adaptor.addChild(root_1,
                                    stream_directDeclaratorLength.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "directDeclaratorSuffix"

    public static class directDeclaratorLength_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "directDeclaratorLength"
    // Grammar/CTFParser.g:775:1: directDeclaratorLength : unaryExpression ;
    public final TSDLParser.directDeclaratorLength_return directDeclaratorLength()
            throws RecognitionException {
        TSDLParser.directDeclaratorLength_return retval = new TSDLParser.directDeclaratorLength_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.unaryExpression_return unaryExpression134 = null;

        try {
            // Grammar/CTFParser.g:775:24: ( unaryExpression )
            // Grammar/CTFParser.g:776:3: unaryExpression
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_unaryExpression_in_directDeclaratorLength2711);
                unaryExpression134 = unaryExpression();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, unaryExpression134.getTree());
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "directDeclaratorLength"

    public static class abstractDeclarator_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "abstractDeclarator"
    // Grammar/CTFParser.g:780:1: abstractDeclarator : ( ( ( pointer )+ (
    // directAbstractDeclarator )? ) -> ^( TYPE_DECLARATOR ( pointer )+ (
    // directAbstractDeclarator )? ) | directAbstractDeclarator -> ^(
    // TYPE_DECLARATOR directAbstractDeclarator ) );
    public final TSDLParser.abstractDeclarator_return abstractDeclarator()
            throws RecognitionException {
        TSDLParser.abstractDeclarator_return retval = new TSDLParser.abstractDeclarator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.pointer_return pointer135 = null;

        TSDLParser.directAbstractDeclarator_return directAbstractDeclarator136 = null;

        TSDLParser.directAbstractDeclarator_return directAbstractDeclarator137 = null;

        RewriteRuleSubtreeStream stream_pointer = new RewriteRuleSubtreeStream(
                adaptor, "rule pointer");
        RewriteRuleSubtreeStream stream_directAbstractDeclarator = new RewriteRuleSubtreeStream(
                adaptor, "rule directAbstractDeclarator");

        enter("abstractDeclarator");

        try {
            // Grammar/CTFParser.g:787:1: ( ( ( pointer )+ (
            // directAbstractDeclarator )? ) -> ^( TYPE_DECLARATOR ( pointer )+
            // ( directAbstractDeclarator )? ) | directAbstractDeclarator -> ^(
            // TYPE_DECLARATOR directAbstractDeclarator ) )
            int alt43 = 2;
            int LA43_0 = input.LA(1);

            if ((LA43_0 == POINTER)) {
                alt43 = 1;
            } else if (((LA43_0 == LPAREN) || (LA43_0 == IDENTIFIER))) {
                alt43 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 43, 0,
                        input);

                throw nvae;
            }
            switch (alt43) {
            case 1:
            // Grammar/CTFParser.g:788:5: ( ( pointer )+ (
            // directAbstractDeclarator )? )
            {
                // Grammar/CTFParser.g:788:5: ( ( pointer )+ (
                // directAbstractDeclarator )? )
                // Grammar/CTFParser.g:788:6: ( pointer )+ (
                // directAbstractDeclarator )?
                {
                    // Grammar/CTFParser.g:788:6: ( pointer )+
                    int cnt41 = 0;
                    loop41: do {
                        int alt41 = 2;
                        int LA41_0 = input.LA(1);

                        if ((LA41_0 == POINTER)) {
                            alt41 = 1;
                        }

                        switch (alt41) {
                        case 1:
                        // Grammar/CTFParser.g:788:6: pointer
                        {
                            pushFollow(FOLLOW_pointer_in_abstractDeclarator2742);
                            pointer135 = pointer();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_pointer.add(pointer135.getTree());
                            }

                        }
                            break;

                        default:
                            if (cnt41 >= 1) {
                                break loop41;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return retval;
                            }
                            EarlyExitException eee = new EarlyExitException(41,
                                    input);
                            throw eee;
                        }
                        cnt41++;
                    } while (true);

                    // Grammar/CTFParser.g:788:15: ( directAbstractDeclarator )?
                    int alt42 = 2;
                    int LA42_0 = input.LA(1);

                    if (((LA42_0 == LPAREN) || (LA42_0 == IDENTIFIER))) {
                        alt42 = 1;
                    }
                    switch (alt42) {
                    case 1:
                    // Grammar/CTFParser.g:788:15: directAbstractDeclarator
                    {
                        pushFollow(FOLLOW_directAbstractDeclarator_in_abstractDeclarator2745);
                        directAbstractDeclarator136 = directAbstractDeclarator();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            stream_directAbstractDeclarator
                                    .add(directAbstractDeclarator136.getTree());
                        }

                    }
                        break;

                    }

                }

                // AST REWRITE
                // elements: pointer, directAbstractDeclarator
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 788:42: -> ^( TYPE_DECLARATOR ( pointer )+ (
                    // directAbstractDeclarator )? )
                    {
                        // Grammar/CTFParser.g:788:45: ^( TYPE_DECLARATOR (
                        // pointer )+ ( directAbstractDeclarator )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(TYPE_DECLARATOR,
                                            "TYPE_DECLARATOR"), root_1);

                            if (!(stream_pointer.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_pointer.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_pointer.nextTree());

                            }
                            stream_pointer.reset();
                            // Grammar/CTFParser.g:788:72: (
                            // directAbstractDeclarator )?
                            if (stream_directAbstractDeclarator.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_directAbstractDeclarator
                                                .nextTree());

                            }
                            stream_directAbstractDeclarator.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 2:
            // Grammar/CTFParser.g:789:5: directAbstractDeclarator
            {
                pushFollow(FOLLOW_directAbstractDeclarator_in_abstractDeclarator2765);
                directAbstractDeclarator137 = directAbstractDeclarator();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_directAbstractDeclarator
                            .add(directAbstractDeclarator137.getTree());
                }

                // AST REWRITE
                // elements: directAbstractDeclarator
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 789:30: -> ^( TYPE_DECLARATOR directAbstractDeclarator )
                    {
                        // Grammar/CTFParser.g:789:33: ^( TYPE_DECLARATOR
                        // directAbstractDeclarator )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(TYPE_DECLARATOR,
                                            "TYPE_DECLARATOR"), root_1);

                            adaptor.addChild(root_1,
                                    stream_directAbstractDeclarator.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("abstractDeclarator");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "abstractDeclarator"

    public static class directAbstractDeclarator_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "directAbstractDeclarator"
    // Grammar/CTFParser.g:796:1: directAbstractDeclarator : ( IDENTIFIER | (
    // LPAREN abstractDeclarator RPAREN ) ) ( OPENBRAC ( unaryExpression )?
    // CLOSEBRAC )? ;
    public final TSDLParser.directAbstractDeclarator_return directAbstractDeclarator()
            throws RecognitionException {
        TSDLParser.directAbstractDeclarator_return retval = new TSDLParser.directAbstractDeclarator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENTIFIER138 = null;
        Token LPAREN139 = null;
        Token RPAREN141 = null;
        Token OPENBRAC142 = null;
        Token CLOSEBRAC144 = null;
        TSDLParser.abstractDeclarator_return abstractDeclarator140 = null;

        TSDLParser.unaryExpression_return unaryExpression143 = null;

        CommonTree IDENTIFIER138_tree = null;
        CommonTree LPAREN139_tree = null;
        CommonTree RPAREN141_tree = null;
        CommonTree OPENBRAC142_tree = null;
        CommonTree CLOSEBRAC144_tree = null;

        enter("directAbstractDeclarator");

        try {
            // Grammar/CTFParser.g:804:1: ( ( IDENTIFIER | ( LPAREN
            // abstractDeclarator RPAREN ) ) ( OPENBRAC ( unaryExpression )?
            // CLOSEBRAC )? )
            // Grammar/CTFParser.g:805:3: ( IDENTIFIER | ( LPAREN
            // abstractDeclarator RPAREN ) ) ( OPENBRAC ( unaryExpression )?
            // CLOSEBRAC )?
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:805:3: ( IDENTIFIER | ( LPAREN
                // abstractDeclarator RPAREN ) )
                int alt44 = 2;
                int LA44_0 = input.LA(1);

                if ((LA44_0 == IDENTIFIER)) {
                    alt44 = 1;
                } else if ((LA44_0 == LPAREN)) {
                    alt44 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            44, 0, input);

                    throw nvae;
                }
                switch (alt44) {
                case 1:
                // Grammar/CTFParser.g:806:6: IDENTIFIER
                {
                    IDENTIFIER138 = (Token) match(input, IDENTIFIER,
                            FOLLOW_IDENTIFIER_in_directAbstractDeclarator2805);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        IDENTIFIER138_tree = (CommonTree) adaptor
                                .create(IDENTIFIER138);
                        adaptor.addChild(root_0, IDENTIFIER138_tree);
                    }

                }
                    break;
                case 2:
                // Grammar/CTFParser.g:807:7: ( LPAREN abstractDeclarator RPAREN
                // )
                {
                    // Grammar/CTFParser.g:807:7: ( LPAREN abstractDeclarator
                    // RPAREN )
                    // Grammar/CTFParser.g:807:8: LPAREN abstractDeclarator
                    // RPAREN
                    {
                        LPAREN139 = (Token) match(input, LPAREN,
                                FOLLOW_LPAREN_in_directAbstractDeclarator2814);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            LPAREN139_tree = (CommonTree) adaptor
                                    .create(LPAREN139);
                            adaptor.addChild(root_0, LPAREN139_tree);
                        }
                        pushFollow(FOLLOW_abstractDeclarator_in_directAbstractDeclarator2816);
                        abstractDeclarator140 = abstractDeclarator();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    abstractDeclarator140.getTree());
                        }
                        RPAREN141 = (Token) match(input, RPAREN,
                                FOLLOW_RPAREN_in_directAbstractDeclarator2818);
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            RPAREN141_tree = (CommonTree) adaptor
                                    .create(RPAREN141);
                            adaptor.addChild(root_0, RPAREN141_tree);
                        }

                    }

                }
                    break;

                }

                // Grammar/CTFParser.g:809:3: ( OPENBRAC ( unaryExpression )?
                // CLOSEBRAC )?
                int alt46 = 2;
                int LA46_0 = input.LA(1);

                if ((LA46_0 == OPENBRAC)) {
                    alt46 = 1;
                }
                switch (alt46) {
                case 1:
                // Grammar/CTFParser.g:810:5: OPENBRAC ( unaryExpression )?
                // CLOSEBRAC
                {
                    OPENBRAC142 = (Token) match(input, OPENBRAC,
                            FOLLOW_OPENBRAC_in_directAbstractDeclarator2833);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        OPENBRAC142_tree = (CommonTree) adaptor
                                .create(OPENBRAC142);
                        adaptor.addChild(root_0, OPENBRAC142_tree);
                    }
                    // Grammar/CTFParser.g:810:14: ( unaryExpression )?
                    int alt45 = 2;
                    int LA45_0 = input.LA(1);

                    if (((LA45_0 == ALIGNTOK) || (LA45_0 == EVENTTOK)
                            || ((LA45_0 >= SIGNEDTOK) && (LA45_0 <= STRINGTOK))
                            || (LA45_0 == TRACETOK)
                            || ((LA45_0 >= ENVTOK) && (LA45_0 <= CLOCKTOK))
                            || (LA45_0 == SIGN) || (LA45_0 == OCTAL_LITERAL)
                            || (LA45_0 == DECIMAL_LITERAL)
                            || (LA45_0 == HEX_LITERAL)
                            || (LA45_0 == CHARACTER_LITERAL)
                            || (LA45_0 == STRING_LITERAL) || (LA45_0 == IDENTIFIER))) {
                        alt45 = 1;
                    }
                    switch (alt45) {
                    case 1:
                    // Grammar/CTFParser.g:810:14: unaryExpression
                    {
                        pushFollow(FOLLOW_unaryExpression_in_directAbstractDeclarator2835);
                        unaryExpression143 = unaryExpression();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    unaryExpression143.getTree());
                        }

                    }
                        break;

                    }

                    CLOSEBRAC144 = (Token) match(input, CLOSEBRAC,
                            FOLLOW_CLOSEBRAC_in_directAbstractDeclarator2838);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        CLOSEBRAC144_tree = (CommonTree) adaptor
                                .create(CLOSEBRAC144);
                        adaptor.addChild(root_0, CLOSEBRAC144_tree);
                    }

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("directAbstractDeclarator");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "directAbstractDeclarator"

    public static class pointer_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "pointer"
    // Grammar/CTFParser.g:814:1: pointer : POINTER ( typeQualifierList )? -> ^(
    // POINTER ( typeQualifierList )? ) ;
    public final TSDLParser.pointer_return pointer() throws RecognitionException {
        TSDLParser.pointer_return retval = new TSDLParser.pointer_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token POINTER145 = null;
        TSDLParser.typeQualifierList_return typeQualifierList146 = null;

        CommonTree POINTER145_tree = null;
        RewriteRuleTokenStream stream_POINTER = new RewriteRuleTokenStream(
                adaptor, "token POINTER");
        RewriteRuleSubtreeStream stream_typeQualifierList = new RewriteRuleSubtreeStream(
                adaptor, "rule typeQualifierList");

        enter("pointer");

        try {
            // Grammar/CTFParser.g:822:1: ( POINTER ( typeQualifierList )? -> ^(
            // POINTER ( typeQualifierList )? ) )
            // Grammar/CTFParser.g:823:3: POINTER ( typeQualifierList )?
            {
                POINTER145 = (Token) match(input, POINTER,
                        FOLLOW_POINTER_in_pointer2866);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_POINTER.add(POINTER145);
                }

                // Grammar/CTFParser.g:823:11: ( typeQualifierList )?
                int alt47 = 2;
                int LA47_0 = input.LA(1);

                if ((LA47_0 == CONSTTOK)) {
                    alt47 = 1;
                }
                switch (alt47) {
                case 1:
                // Grammar/CTFParser.g:823:11: typeQualifierList
                {
                    pushFollow(FOLLOW_typeQualifierList_in_pointer2868);
                    typeQualifierList146 = typeQualifierList();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_typeQualifierList.add(typeQualifierList146
                                .getTree());
                    }

                }
                    break;

                }

                // AST REWRITE
                // elements: typeQualifierList, POINTER
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 823:30: -> ^( POINTER ( typeQualifierList )? )
                    {
                        // Grammar/CTFParser.g:823:33: ^( POINTER (
                        // typeQualifierList )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    stream_POINTER.nextNode(), root_1);

                            // Grammar/CTFParser.g:823:43: ( typeQualifierList
                            // )?
                            if (stream_typeQualifierList.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_typeQualifierList.nextTree());

                            }
                            stream_typeQualifierList.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("pointer");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "pointer"

    public static class typeQualifierList_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "typeQualifierList"
    // Grammar/CTFParser.g:826:1: typeQualifierList : ( typeQualifier )+ ;
    public final TSDLParser.typeQualifierList_return typeQualifierList()
            throws RecognitionException {
        TSDLParser.typeQualifierList_return retval = new TSDLParser.typeQualifierList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.typeQualifier_return typeQualifier147 = null;

        try {
            // Grammar/CTFParser.g:826:19: ( ( typeQualifier )+ )
            // Grammar/CTFParser.g:827:3: ( typeQualifier )+
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:827:3: ( typeQualifier )+
                int cnt48 = 0;
                loop48: do {
                    int alt48 = 2;
                    int LA48_0 = input.LA(1);

                    if ((LA48_0 == CONSTTOK)) {
                        alt48 = 1;
                    }

                    switch (alt48) {
                    case 1:
                    // Grammar/CTFParser.g:827:3: typeQualifier
                    {
                        pushFollow(FOLLOW_typeQualifier_in_typeQualifierList2891);
                        typeQualifier147 = typeQualifier();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0, typeQualifier147.getTree());
                        }

                    }
                        break;

                    default:
                        if (cnt48 >= 1) {
                            break loop48;
                        }
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        EarlyExitException eee = new EarlyExitException(48,
                                input);
                        throw eee;
                    }
                    cnt48++;
                } while (true);

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "typeQualifierList"

    public static class typedefName_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "typedefName"
    // Grammar/CTFParser.g:830:1: typedefName : {...}? IDENTIFIER ;
    public final TSDLParser.typedefName_return typedefName()
            throws RecognitionException {
        TSDLParser.typedefName_return retval = new TSDLParser.typedefName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENTIFIER148 = null;

        CommonTree IDENTIFIER148_tree = null;

        enter("typedefName");

        try {
            // Grammar/CTFParser.g:838:1: ({...}? IDENTIFIER )
            // Grammar/CTFParser.g:839:3: {...}? IDENTIFIER
            {
                root_0 = (CommonTree) adaptor.nil();

                if (!((inTypealiasAlias() || isTypeName(input.LT(1).getText())))) {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    throw new FailedPredicateException(input, "typedefName",
                            "inTypealiasAlias() || isTypeName(input.LT(1).getText())");
                }
                IDENTIFIER148 = (Token) match(input, IDENTIFIER,
                        FOLLOW_IDENTIFIER_in_typedefName2917);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    IDENTIFIER148_tree = (CommonTree) adaptor
                            .create(IDENTIFIER148);
                    adaptor.addChild(root_0, IDENTIFIER148_tree);
                }
                if (state.backtracking == 0) {
                    if ((inTypedef() || inTypealiasAlias())
                            && !isTypeName((IDENTIFIER148 != null ? IDENTIFIER148
                                    .getText() : null))) {
                        addTypeName((IDENTIFIER148 != null ? IDENTIFIER148
                                .getText() : null));
                    }
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print("typedefName: "
                        + input.toString(retval.start, input.LT(-1)));
                exit("typedefName");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "typedefName"

    public static class typealiasTarget_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "typealiasTarget"
    // Grammar/CTFParser.g:842:1: typealiasTarget : declarationSpecifiers (
    // abstractDeclaratorList )? ;
    public final TSDLParser.typealiasTarget_return typealiasTarget()
            throws RecognitionException {
        TSDLParser.typealiasTarget_return retval = new TSDLParser.typealiasTarget_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.declarationSpecifiers_return declarationSpecifiers149 = null;

        TSDLParser.abstractDeclaratorList_return abstractDeclaratorList150 = null;

        enter("typealiasTarget");

        try {
            // Grammar/CTFParser.g:855:1: ( declarationSpecifiers (
            // abstractDeclaratorList )? )
            // Grammar/CTFParser.g:856:3: declarationSpecifiers (
            // abstractDeclaratorList )?
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_declarationSpecifiers_in_typealiasTarget2945);
                declarationSpecifiers149 = declarationSpecifiers();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, declarationSpecifiers149.getTree());
                }
                // Grammar/CTFParser.g:856:25: ( abstractDeclaratorList )?
                int alt49 = 2;
                int LA49_0 = input.LA(1);

                if (((LA49_0 == LPAREN) || (LA49_0 == POINTER) || (LA49_0 == IDENTIFIER))) {
                    alt49 = 1;
                }
                switch (alt49) {
                case 1:
                // Grammar/CTFParser.g:856:25: abstractDeclaratorList
                {
                    pushFollow(FOLLOW_abstractDeclaratorList_in_typealiasTarget2947);
                    abstractDeclaratorList150 = abstractDeclaratorList();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        adaptor.addChild(root_0,
                                abstractDeclaratorList150.getTree());
                    }

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("typealiasTarget");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "typealiasTarget"

    public static class typealiasAlias_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "typealiasAlias"
    // Grammar/CTFParser.g:859:1: typealiasAlias : ( abstractDeclaratorList | (
    // declarationSpecifiers ( abstractDeclaratorList )? ) ) ;
    public final TSDLParser.typealiasAlias_return typealiasAlias()
            throws RecognitionException {
        TSDLParser.typealiasAlias_return retval = new TSDLParser.typealiasAlias_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.abstractDeclaratorList_return abstractDeclaratorList151 = null;

        TSDLParser.declarationSpecifiers_return declarationSpecifiers152 = null;

        TSDLParser.abstractDeclaratorList_return abstractDeclaratorList153 = null;

        enter("typealiasAlias");
        typealiasAliasOn();

        try {
            // Grammar/CTFParser.g:874:1: ( ( abstractDeclaratorList | (
            // declarationSpecifiers ( abstractDeclaratorList )? ) ) )
            // Grammar/CTFParser.g:875:3: ( abstractDeclaratorList | (
            // declarationSpecifiers ( abstractDeclaratorList )? ) )
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:875:3: ( abstractDeclaratorList | (
                // declarationSpecifiers ( abstractDeclaratorList )? ) )
                int alt51 = 2;
                switch (input.LA(1)) {
                case LPAREN:
                case POINTER: {
                    alt51 = 1;
                }
                    break;
                case IDENTIFIER: {
                    int LA51_2 = input.LA(2);

                    if ((!(((inTypealiasAlias() || isTypeName(input.LT(1)
                            .getText())))))) {
                        alt51 = 1;
                    } else if (((inTypealiasAlias() || isTypeName(input.LT(1)
                            .getText())))) {
                        alt51 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae = new NoViableAltException(
                                "", 51, 2, input);

                        throw nvae;
                    }
                }
                    break;
                case CONSTTOK:
                case CHARTOK:
                case DOUBLETOK:
                case ENUMTOK:
                case FLOATINGPOINTTOK:
                case FLOATTOK:
                case INTEGERTOK:
                case INTTOK:
                case LONGTOK:
                case SHORTTOK:
                case SIGNEDTOK:
                case STRINGTOK:
                case STRUCTTOK:
                case TYPEDEFTOK:
                case UNSIGNEDTOK:
                case VARIANTTOK:
                case VOIDTOK:
                case BOOLTOK:
                case COMPLEXTOK:
                case IMAGINARYTOK: {
                    alt51 = 2;
                }
                    break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            51, 0, input);

                    throw nvae;
                }

                switch (alt51) {
                case 1:
                // Grammar/CTFParser.g:876:3: abstractDeclaratorList
                {
                    pushFollow(FOLLOW_abstractDeclaratorList_in_typealiasAlias2980);
                    abstractDeclaratorList151 = abstractDeclaratorList();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        adaptor.addChild(root_0,
                                abstractDeclaratorList151.getTree());
                    }

                }
                    break;
                case 2:
                // Grammar/CTFParser.g:878:3: ( declarationSpecifiers (
                // abstractDeclaratorList )? )
                {
                    // Grammar/CTFParser.g:878:3: ( declarationSpecifiers (
                    // abstractDeclaratorList )? )
                    // Grammar/CTFParser.g:878:4: declarationSpecifiers (
                    // abstractDeclaratorList )?
                    {
                        pushFollow(FOLLOW_declarationSpecifiers_in_typealiasAlias2989);
                        declarationSpecifiers152 = declarationSpecifiers();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    declarationSpecifiers152.getTree());
                        }
                        // Grammar/CTFParser.g:878:26: ( abstractDeclaratorList
                        // )?
                        int alt50 = 2;
                        int LA50_0 = input.LA(1);

                        if (((LA50_0 == LPAREN) || (LA50_0 == POINTER) || (LA50_0 == IDENTIFIER))) {
                            alt50 = 1;
                        }
                        switch (alt50) {
                        case 1:
                        // Grammar/CTFParser.g:878:26: abstractDeclaratorList
                        {
                            pushFollow(FOLLOW_abstractDeclaratorList_in_typealiasAlias2991);
                            abstractDeclaratorList153 = abstractDeclaratorList();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                adaptor.addChild(root_0,
                                        abstractDeclaratorList153.getTree());
                            }

                        }
                            break;

                        }

                    }

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("typealiasAlias");
                typealiasAliasOff();

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "typealiasAlias"

    public static class typealiasDecl_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "typealiasDecl"
    // Grammar/CTFParser.g:882:1: typealiasDecl : TYPEALIASTOK typealiasTarget
    // TYPE_ASSIGNMENT typealiasAlias -> ^( TYPEALIAS ^( TYPEALIAS_TARGET
    // typealiasTarget ) ^( TYPEALIAS_ALIAS typealiasAlias ) ) ;
    public final TSDLParser.typealiasDecl_return typealiasDecl()
            throws RecognitionException {
        TSDLParser.typealiasDecl_return retval = new TSDLParser.typealiasDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token TYPEALIASTOK154 = null;
        Token TYPE_ASSIGNMENT156 = null;
        TSDLParser.typealiasTarget_return typealiasTarget155 = null;

        TSDLParser.typealiasAlias_return typealiasAlias157 = null;

        CommonTree TYPEALIASTOK154_tree = null;
        CommonTree TYPE_ASSIGNMENT156_tree = null;
        RewriteRuleTokenStream stream_TYPE_ASSIGNMENT = new RewriteRuleTokenStream(
                adaptor, "token TYPE_ASSIGNMENT");
        RewriteRuleTokenStream stream_TYPEALIASTOK = new RewriteRuleTokenStream(
                adaptor, "token TYPEALIASTOK");
        RewriteRuleSubtreeStream stream_typealiasAlias = new RewriteRuleSubtreeStream(
                adaptor, "rule typealiasAlias");
        RewriteRuleSubtreeStream stream_typealiasTarget = new RewriteRuleSubtreeStream(
                adaptor, "rule typealiasTarget");

        enter("typealiasDecl");

        try {
            // Grammar/CTFParser.g:889:1: ( TYPEALIASTOK typealiasTarget
            // TYPE_ASSIGNMENT typealiasAlias -> ^( TYPEALIAS ^(
            // TYPEALIAS_TARGET typealiasTarget ) ^( TYPEALIAS_ALIAS
            // typealiasAlias ) ) )
            // Grammar/CTFParser.g:890:3: TYPEALIASTOK typealiasTarget
            // TYPE_ASSIGNMENT typealiasAlias
            {
                TYPEALIASTOK154 = (Token) match(input, TYPEALIASTOK,
                        FOLLOW_TYPEALIASTOK_in_typealiasDecl3023);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_TYPEALIASTOK.add(TYPEALIASTOK154);
                }

                pushFollow(FOLLOW_typealiasTarget_in_typealiasDecl3025);
                typealiasTarget155 = typealiasTarget();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_typealiasTarget.add(typealiasTarget155.getTree());
                }
                TYPE_ASSIGNMENT156 = (Token) match(input, TYPE_ASSIGNMENT,
                        FOLLOW_TYPE_ASSIGNMENT_in_typealiasDecl3027);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_TYPE_ASSIGNMENT.add(TYPE_ASSIGNMENT156);
                }

                pushFollow(FOLLOW_typealiasAlias_in_typealiasDecl3029);
                typealiasAlias157 = typealiasAlias();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_typealiasAlias.add(typealiasAlias157.getTree());
                }

                // AST REWRITE
                // elements: typealiasTarget, typealiasAlias
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 891:3: -> ^( TYPEALIAS ^( TYPEALIAS_TARGET
                    // typealiasTarget ) ^( TYPEALIAS_ALIAS typealiasAlias ) )
                    {
                        // Grammar/CTFParser.g:891:6: ^( TYPEALIAS ^(
                        // TYPEALIAS_TARGET typealiasTarget ) ^( TYPEALIAS_ALIAS
                        // typealiasAlias ) )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(TYPEALIAS, "TYPEALIAS"),
                                    root_1);

                            // Grammar/CTFParser.g:891:18: ^( TYPEALIAS_TARGET
                            // typealiasTarget )
                            {
                                CommonTree root_2 = (CommonTree) adaptor.nil();
                                root_2 = (CommonTree) adaptor.becomeRoot(
                                        adaptor.create(TYPEALIAS_TARGET,
                                                "TYPEALIAS_TARGET"), root_2);

                                adaptor.addChild(root_2,
                                        stream_typealiasTarget.nextTree());

                                adaptor.addChild(root_1, root_2);
                            }
                            // Grammar/CTFParser.g:891:54: ^( TYPEALIAS_ALIAS
                            // typealiasAlias )
                            {
                                CommonTree root_2 = (CommonTree) adaptor.nil();
                                root_2 = (CommonTree) adaptor.becomeRoot(
                                        adaptor.create(TYPEALIAS_ALIAS,
                                                "TYPEALIAS_ALIAS"), root_2);

                                adaptor.addChild(root_2,
                                        stream_typealiasAlias.nextTree());

                                adaptor.addChild(root_1, root_2);
                            }

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("typealiasDecl");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "typealiasDecl"

    public static class ctfKeyword_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "ctfKeyword"
    // Grammar/CTFParser.g:897:1: ctfKeyword : ( ALIGNTOK | EVENTTOK | SIGNEDTOK
    // | STRINGTOK );
    public final TSDLParser.ctfKeyword_return ctfKeyword()
            throws RecognitionException {
        TSDLParser.ctfKeyword_return retval = new TSDLParser.ctfKeyword_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set158 = null;

        CommonTree set158_tree = null;

        enter("ctfKeyword");

        try {
            // Grammar/CTFParser.g:905:1: ( ALIGNTOK | EVENTTOK | SIGNEDTOK |
            // STRINGTOK )
            // Grammar/CTFParser.g:
            {
                root_0 = (CommonTree) adaptor.nil();

                set158 = input.LT(1);
                if ((input.LA(1) == ALIGNTOK) || (input.LA(1) == EVENTTOK)
                        || (input.LA(1) == SIGNEDTOK)
                        || (input.LA(1) == STRINGTOK)) {
                    input.consume();
                    if (state.backtracking == 0) {
                        adaptor.addChild(root_0, adaptor.create(set158));
                    }
                    state.errorRecovery = false;
                    state.failed = false;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    MismatchedSetException mse = new MismatchedSetException(
                            null, input);
                    throw mse;
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("ctfKeyword");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "ctfKeyword"

    public static class ctfSpecifier_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "ctfSpecifier"
    // Grammar/CTFParser.g:912:1: ctfSpecifier : ( ctfSpecifierHead ctfBody ->
    // ^( ctfSpecifierHead ctfBody ) | typealiasDecl -> ^( DECLARATION
    // typealiasDecl ) );
    public final TSDLParser.ctfSpecifier_return ctfSpecifier()
            throws RecognitionException {
        TSDLParser.ctfSpecifier_return retval = new TSDLParser.ctfSpecifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TSDLParser.ctfSpecifierHead_return ctfSpecifierHead159 = null;

        TSDLParser.ctfBody_return ctfBody160 = null;

        TSDLParser.typealiasDecl_return typealiasDecl161 = null;

        RewriteRuleSubtreeStream stream_ctfSpecifierHead = new RewriteRuleSubtreeStream(
                adaptor, "rule ctfSpecifierHead");
        RewriteRuleSubtreeStream stream_typealiasDecl = new RewriteRuleSubtreeStream(
                adaptor, "rule typealiasDecl");
        RewriteRuleSubtreeStream stream_ctfBody = new RewriteRuleSubtreeStream(
                adaptor, "rule ctfBody");

        enter("ctfSpecifier");

        try {
            // Grammar/CTFParser.g:919:3: ( ctfSpecifierHead ctfBody -> ^(
            // ctfSpecifierHead ctfBody ) | typealiasDecl -> ^( DECLARATION
            // typealiasDecl ) )
            int alt52 = 2;
            int LA52_0 = input.LA(1);

            if (((LA52_0 == EVENTTOK) || (LA52_0 == STREAMTOK)
                    || (LA52_0 == TRACETOK) || ((LA52_0 >= ENVTOK) && (LA52_0 <= CLOCKTOK)))) {
                alt52 = 1;
            } else if ((LA52_0 == TYPEALIASTOK)) {
                alt52 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 52, 0,
                        input);

                throw nvae;
            }
            switch (alt52) {
            case 1:
            // Grammar/CTFParser.g:921:3: ctfSpecifierHead ctfBody
            {
                pushFollow(FOLLOW_ctfSpecifierHead_in_ctfSpecifier3123);
                ctfSpecifierHead159 = ctfSpecifierHead();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ctfSpecifierHead.add(ctfSpecifierHead159.getTree());
                }
                pushFollow(FOLLOW_ctfBody_in_ctfSpecifier3125);
                ctfBody160 = ctfBody();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ctfBody.add(ctfBody160.getTree());
                }

                // AST REWRITE
                // elements: ctfBody, ctfSpecifierHead
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 921:28: -> ^( ctfSpecifierHead ctfBody )
                    {
                        // Grammar/CTFParser.g:921:31: ^( ctfSpecifierHead
                        // ctfBody )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    stream_ctfSpecifierHead.nextNode(), root_1);

                            adaptor.addChild(root_1, stream_ctfBody.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 2:
            // Grammar/CTFParser.g:924:3: typealiasDecl
            {
                pushFollow(FOLLOW_typealiasDecl_in_ctfSpecifier3144);
                typealiasDecl161 = typealiasDecl();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_typealiasDecl.add(typealiasDecl161.getTree());
                }

                // AST REWRITE
                // elements: typealiasDecl
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 924:17: -> ^( DECLARATION typealiasDecl )
                    {
                        // Grammar/CTFParser.g:924:20: ^( DECLARATION
                        // typealiasDecl )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(DECLARATION, "DECLARATION"),
                                    root_1);

                            adaptor.addChild(root_1,
                                    stream_typealiasDecl.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("ctfSpecifier");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "ctfSpecifier"

    public static class ctfSpecifierHead_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "ctfSpecifierHead"
    // Grammar/CTFParser.g:927:1: ctfSpecifierHead : ( EVENTTOK -> EVENT |
    // STREAMTOK -> STREAM | TRACETOK -> TRACE | ENVTOK -> ENV | CLOCKTOK ->
    // CLOCK );
    public final TSDLParser.ctfSpecifierHead_return ctfSpecifierHead()
            throws RecognitionException {
        TSDLParser.ctfSpecifierHead_return retval = new TSDLParser.ctfSpecifierHead_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EVENTTOK162 = null;
        Token STREAMTOK163 = null;
        Token TRACETOK164 = null;
        Token ENVTOK165 = null;
        Token CLOCKTOK166 = null;

        CommonTree EVENTTOK162_tree = null;
        CommonTree STREAMTOK163_tree = null;
        CommonTree TRACETOK164_tree = null;
        CommonTree ENVTOK165_tree = null;
        CommonTree CLOCKTOK166_tree = null;
        RewriteRuleTokenStream stream_EVENTTOK = new RewriteRuleTokenStream(
                adaptor, "token EVENTTOK");
        RewriteRuleTokenStream stream_STREAMTOK = new RewriteRuleTokenStream(
                adaptor, "token STREAMTOK");
        RewriteRuleTokenStream stream_ENVTOK = new RewriteRuleTokenStream(
                adaptor, "token ENVTOK");
        RewriteRuleTokenStream stream_CLOCKTOK = new RewriteRuleTokenStream(
                adaptor, "token CLOCKTOK");
        RewriteRuleTokenStream stream_TRACETOK = new RewriteRuleTokenStream(
                adaptor, "token TRACETOK");

        enter("ctfSpecifierHead");

        try {
            // Grammar/CTFParser.g:935:1: ( EVENTTOK -> EVENT | STREAMTOK ->
            // STREAM | TRACETOK -> TRACE | ENVTOK -> ENV | CLOCKTOK -> CLOCK )
            int alt53 = 5;
            switch (input.LA(1)) {
            case EVENTTOK: {
                alt53 = 1;
            }
                break;
            case STREAMTOK: {
                alt53 = 2;
            }
                break;
            case TRACETOK: {
                alt53 = 3;
            }
                break;
            case ENVTOK: {
                alt53 = 4;
            }
                break;
            case CLOCKTOK: {
                alt53 = 5;
            }
                break;
            default:
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 53, 0,
                        input);

                throw nvae;
            }

            switch (alt53) {
            case 1:
            // Grammar/CTFParser.g:936:4: EVENTTOK
            {
                EVENTTOK162 = (Token) match(input, EVENTTOK,
                        FOLLOW_EVENTTOK_in_ctfSpecifierHead3176);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_EVENTTOK.add(EVENTTOK162);
                }

                // AST REWRITE
                // elements:
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 936:13: -> EVENT
                    {
                        adaptor.addChild(root_0, adaptor.create(EVENT, "EVENT"));

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 2:
            // Grammar/CTFParser.g:937:4: STREAMTOK
            {
                STREAMTOK163 = (Token) match(input, STREAMTOK,
                        FOLLOW_STREAMTOK_in_ctfSpecifierHead3185);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_STREAMTOK.add(STREAMTOK163);
                }

                // AST REWRITE
                // elements:
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 937:14: -> STREAM
                    {
                        adaptor.addChild(root_0,
                                adaptor.create(STREAM, "STREAM"));

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 3:
            // Grammar/CTFParser.g:938:4: TRACETOK
            {
                TRACETOK164 = (Token) match(input, TRACETOK,
                        FOLLOW_TRACETOK_in_ctfSpecifierHead3194);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_TRACETOK.add(TRACETOK164);
                }

                // AST REWRITE
                // elements:
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 938:13: -> TRACE
                    {
                        adaptor.addChild(root_0, adaptor.create(TRACE, "TRACE"));

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 4:
            // Grammar/CTFParser.g:939:4: ENVTOK
            {
                ENVTOK165 = (Token) match(input, ENVTOK,
                        FOLLOW_ENVTOK_in_ctfSpecifierHead3203);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ENVTOK.add(ENVTOK165);
                }

                // AST REWRITE
                // elements:
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 939:11: -> ENV
                    {
                        adaptor.addChild(root_0, adaptor.create(ENV, "ENV"));

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 5:
            // Grammar/CTFParser.g:940:4: CLOCKTOK
            {
                CLOCKTOK166 = (Token) match(input, CLOCKTOK,
                        FOLLOW_CLOCKTOK_in_ctfSpecifierHead3212);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_CLOCKTOK.add(CLOCKTOK166);
                }

                // AST REWRITE
                // elements:
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 940:13: -> CLOCK
                    {
                        adaptor.addChild(root_0, adaptor.create(CLOCK, "CLOCK"));

                    }

                    retval.tree = root_0;
                }
            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print(input.toString(retval.start, input.LT(-1)));
                exit("ctfSpecifierHead");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "ctfSpecifierHead"

    public static class ctfTypeSpecifier_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "ctfTypeSpecifier"
    // Grammar/CTFParser.g:943:1: ctfTypeSpecifier : ( FLOATINGPOINTTOK ctfBody
    // -> ^( FLOATING_POINT ( ctfBody )? ) | INTEGERTOK ctfBody -> ^( INTEGER (
    // ctfBody )? ) | STRINGTOK ( ctfBody )? -> ^( STRING ( ctfBody )? ) );
    public final TSDLParser.ctfTypeSpecifier_return ctfTypeSpecifier()
            throws RecognitionException {
        TSDLParser.ctfTypeSpecifier_return retval = new TSDLParser.ctfTypeSpecifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token FLOATINGPOINTTOK167 = null;
        Token INTEGERTOK169 = null;
        Token STRINGTOK171 = null;
        TSDLParser.ctfBody_return ctfBody168 = null;

        TSDLParser.ctfBody_return ctfBody170 = null;

        TSDLParser.ctfBody_return ctfBody172 = null;

        CommonTree FLOATINGPOINTTOK167_tree = null;
        CommonTree INTEGERTOK169_tree = null;
        CommonTree STRINGTOK171_tree = null;
        RewriteRuleTokenStream stream_FLOATINGPOINTTOK = new RewriteRuleTokenStream(
                adaptor, "token FLOATINGPOINTTOK");
        RewriteRuleTokenStream stream_STRINGTOK = new RewriteRuleTokenStream(
                adaptor, "token STRINGTOK");
        RewriteRuleTokenStream stream_INTEGERTOK = new RewriteRuleTokenStream(
                adaptor, "token INTEGERTOK");
        RewriteRuleSubtreeStream stream_ctfBody = new RewriteRuleSubtreeStream(
                adaptor, "rule ctfBody");

        enter("ctfTypeSpecifier");

        try {
            // Grammar/CTFParser.g:950:1: ( FLOATINGPOINTTOK ctfBody -> ^(
            // FLOATING_POINT ( ctfBody )? ) | INTEGERTOK ctfBody -> ^( INTEGER
            // ( ctfBody )? ) | STRINGTOK ( ctfBody )? -> ^( STRING ( ctfBody )?
            // ) )
            int alt55 = 3;
            switch (input.LA(1)) {
            case FLOATINGPOINTTOK: {
                alt55 = 1;
            }
                break;
            case INTEGERTOK: {
                alt55 = 2;
            }
                break;
            case STRINGTOK: {
                alt55 = 3;
            }
                break;
            default:
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 55, 0,
                        input);

                throw nvae;
            }

            switch (alt55) {
            case 1:
            // Grammar/CTFParser.g:952:5: FLOATINGPOINTTOK ctfBody
            {
                FLOATINGPOINTTOK167 = (Token) match(input, FLOATINGPOINTTOK,
                        FOLLOW_FLOATINGPOINTTOK_in_ctfTypeSpecifier3246);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_FLOATINGPOINTTOK.add(FLOATINGPOINTTOK167);
                }

                pushFollow(FOLLOW_ctfBody_in_ctfTypeSpecifier3248);
                ctfBody168 = ctfBody();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ctfBody.add(ctfBody168.getTree());
                }

                // AST REWRITE
                // elements: ctfBody
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 952:30: -> ^( FLOATING_POINT ( ctfBody )? )
                    {
                        // Grammar/CTFParser.g:952:33: ^( FLOATING_POINT (
                        // ctfBody )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(adaptor
                                    .create(FLOATING_POINT, "FLOATING_POINT"),
                                    root_1);

                            // Grammar/CTFParser.g:952:50: ( ctfBody )?
                            if (stream_ctfBody.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_ctfBody.nextTree());

                            }
                            stream_ctfBody.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 2:
            // Grammar/CTFParser.g:953:5: INTEGERTOK ctfBody
            {
                INTEGERTOK169 = (Token) match(input, INTEGERTOK,
                        FOLLOW_INTEGERTOK_in_ctfTypeSpecifier3263);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_INTEGERTOK.add(INTEGERTOK169);
                }

                pushFollow(FOLLOW_ctfBody_in_ctfTypeSpecifier3265);
                ctfBody170 = ctfBody();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_ctfBody.add(ctfBody170.getTree());
                }

                // AST REWRITE
                // elements: ctfBody
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 953:24: -> ^( INTEGER ( ctfBody )? )
                    {
                        // Grammar/CTFParser.g:953:27: ^( INTEGER ( ctfBody )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(INTEGER, "INTEGER"), root_1);

                            // Grammar/CTFParser.g:953:37: ( ctfBody )?
                            if (stream_ctfBody.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_ctfBody.nextTree());

                            }
                            stream_ctfBody.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 3:
            // Grammar/CTFParser.g:954:5: STRINGTOK ( ctfBody )?
            {
                STRINGTOK171 = (Token) match(input, STRINGTOK,
                        FOLLOW_STRINGTOK_in_ctfTypeSpecifier3280);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_STRINGTOK.add(STRINGTOK171);
                }

                // Grammar/CTFParser.g:954:15: ( ctfBody )?
                int alt54 = 2;
                alt54 = dfa54.predict(input);
                switch (alt54) {
                case 1:
                // Grammar/CTFParser.g:954:15: ctfBody
                {
                    pushFollow(FOLLOW_ctfBody_in_ctfTypeSpecifier3282);
                    ctfBody172 = ctfBody();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_ctfBody.add(ctfBody172.getTree());
                    }

                }
                    break;

                }

                // AST REWRITE
                // elements: ctfBody
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 954:24: -> ^( STRING ( ctfBody )? )
                    {
                        // Grammar/CTFParser.g:954:27: ^( STRING ( ctfBody )? )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(STRING, "STRING"), root_1);

                            // Grammar/CTFParser.g:954:36: ( ctfBody )?
                            if (stream_ctfBody.hasNext()) {
                                adaptor.addChild(root_1,
                                        stream_ctfBody.nextTree());

                            }
                            stream_ctfBody.reset();

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                exit("ctfTypeSpecifier");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "ctfTypeSpecifier"

    public static class ctfBody_return extends ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "ctfBody"
    // Grammar/CTFParser.g:957:1: ctfBody : LCURL ( ctfAssignmentExpressionList
    // )? RCURL -> ( ctfAssignmentExpressionList )? ;
    public final TSDLParser.ctfBody_return ctfBody() throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        TSDLParser.ctfBody_return retval = new TSDLParser.ctfBody_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LCURL173 = null;
        Token RCURL175 = null;
        TSDLParser.ctfAssignmentExpressionList_return ctfAssignmentExpressionList174 = null;

        CommonTree LCURL173_tree = null;
        CommonTree RCURL175_tree = null;
        RewriteRuleTokenStream stream_LCURL = new RewriteRuleTokenStream(
                adaptor, "token LCURL");
        RewriteRuleTokenStream stream_RCURL = new RewriteRuleTokenStream(
                adaptor, "token RCURL");
        RewriteRuleSubtreeStream stream_ctfAssignmentExpressionList = new RewriteRuleSubtreeStream(
                adaptor, "rule ctfAssignmentExpressionList");

        enter("ctfBody");
        debug_print("Scope push " + +Symbols_stack.size());
        ((Symbols_scope) Symbols_stack.peek()).types = new HashSet<String>();

        try {
            // Grammar/CTFParser.g:968:1: ( LCURL ( ctfAssignmentExpressionList
            // )? RCURL -> ( ctfAssignmentExpressionList )? )
            // Grammar/CTFParser.g:969:3: LCURL ( ctfAssignmentExpressionList )?
            // RCURL
            {
                LCURL173 = (Token) match(input, LCURL,
                        FOLLOW_LCURL_in_ctfBody3320);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_LCURL.add(LCURL173);
                }

                // Grammar/CTFParser.g:969:9: ( ctfAssignmentExpressionList )?
                int alt56 = 2;
                int LA56_0 = input.LA(1);

                if ((((LA56_0 >= ALIGNTOK) && (LA56_0 <= CLOCKTOK))
                        || (LA56_0 == SIGN) || (LA56_0 == OCTAL_LITERAL)
                        || (LA56_0 == DECIMAL_LITERAL)
                        || (LA56_0 == HEX_LITERAL)
                        || (LA56_0 == CHARACTER_LITERAL)
                        || (LA56_0 == STRING_LITERAL) || (LA56_0 == IDENTIFIER))) {
                    alt56 = 1;
                }
                switch (alt56) {
                case 1:
                // Grammar/CTFParser.g:969:9: ctfAssignmentExpressionList
                {
                    pushFollow(FOLLOW_ctfAssignmentExpressionList_in_ctfBody3322);
                    ctfAssignmentExpressionList174 = ctfAssignmentExpressionList();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_ctfAssignmentExpressionList
                                .add(ctfAssignmentExpressionList174.getTree());
                    }

                }
                    break;

                }

                RCURL175 = (Token) match(input, RCURL,
                        FOLLOW_RCURL_in_ctfBody3325);
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    stream_RCURL.add(RCURL175);
                }

                // AST REWRITE
                // elements: ctfAssignmentExpressionList
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
//                    RewriteRuleSubtreeStream stream_retval =
                            new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 969:44: -> ( ctfAssignmentExpressionList )?
                    {
                        // Grammar/CTFParser.g:969:47: (
                        // ctfAssignmentExpressionList )?
                        if (stream_ctfAssignmentExpressionList.hasNext()) {
                            adaptor.addChild(root_0,
                                    stream_ctfAssignmentExpressionList
                                            .nextTree());

                        }
                        stream_ctfAssignmentExpressionList.reset();

                    }

                    retval.tree = root_0;
                }
            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                debug_print("Scope pop " + +Symbols_stack.size());
                exit("ctfBody");

            }
        }

        catch (RecognitionException e) {
            throw e;
        } finally {
            Symbols_stack.pop();

        }
        return retval;
    }

    // $ANTLR end "ctfBody"

    public static class ctfAssignmentExpressionList_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "ctfAssignmentExpressionList"
    // Grammar/CTFParser.g:972:1: ctfAssignmentExpressionList : (
    // ctfAssignmentExpression TERM )+ ;
    public final TSDLParser.ctfAssignmentExpressionList_return ctfAssignmentExpressionList()
            throws RecognitionException {
        TSDLParser.ctfAssignmentExpressionList_return retval = new TSDLParser.ctfAssignmentExpressionList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token TERM177 = null;
        TSDLParser.ctfAssignmentExpression_return ctfAssignmentExpression176 = null;

        CommonTree TERM177_tree = null;

        try {
            // Grammar/CTFParser.g:972:29: ( ( ctfAssignmentExpression TERM )+ )
            // Grammar/CTFParser.g:973:3: ( ctfAssignmentExpression TERM )+
            {
                root_0 = (CommonTree) adaptor.nil();

                // Grammar/CTFParser.g:973:3: ( ctfAssignmentExpression TERM )+
                int cnt57 = 0;
                loop57: do {
                    int alt57 = 2;
                    int LA57_0 = input.LA(1);

                    if ((((LA57_0 >= ALIGNTOK) && (LA57_0 <= CLOCKTOK))
                            || (LA57_0 == SIGN) || (LA57_0 == OCTAL_LITERAL)
                            || (LA57_0 == DECIMAL_LITERAL)
                            || (LA57_0 == HEX_LITERAL)
                            || (LA57_0 == CHARACTER_LITERAL)
                            || (LA57_0 == STRING_LITERAL) || (LA57_0 == IDENTIFIER))) {
                        alt57 = 1;
                    }

                    switch (alt57) {
                    case 1:
                    // Grammar/CTFParser.g:973:4: ctfAssignmentExpression TERM
                    {
                        pushFollow(FOLLOW_ctfAssignmentExpression_in_ctfAssignmentExpressionList3344);
                        ctfAssignmentExpression176 = ctfAssignmentExpression();

                        state._fsp--;
                        if (state.failed) {
                            return retval;
                        }
                        if (state.backtracking == 0) {
                            adaptor.addChild(root_0,
                                    ctfAssignmentExpression176.getTree());
                        }
                        TERM177 = (Token) match(input, TERM,
                                FOLLOW_TERM_in_ctfAssignmentExpressionList3346);
                        if (state.failed) {
                            return retval;
                        }

                    }
                        break;

                    default:
                        if (cnt57 >= 1) {
                            break loop57;
                        }
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        EarlyExitException eee = new EarlyExitException(57,
                                input);
                        throw eee;
                    }
                    cnt57++;
                } while (true);

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "ctfAssignmentExpressionList"

    public static class ctfAssignmentExpression_return extends
            ParserRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "ctfAssignmentExpression"
    // Grammar/CTFParser.g:977:1: ctfAssignmentExpression : ( (left=
    // unaryExpression ( (assignment= ASSIGNMENT right1= unaryExpression ) -> ^(
    // CTF_EXPRESSION_VAL ^( CTF_LEFT $left) ^( CTF_RIGHT $right1) ) |
    // (type_assignment= TYPE_ASSIGNMENT right2= typeSpecifier ) -> ^(
    // CTF_EXPRESSION_TYPE ^( CTF_LEFT $left) ^( CTF_RIGHT ^(
    // TYPE_SPECIFIER_LIST $right2) ) ) ) ) | ( declarationSpecifiers {...}?
    // declaratorList ) -> ^( TYPEDEF declaratorList declarationSpecifiers ) |
    // typealiasDecl );
    public final TSDLParser.ctfAssignmentExpression_return ctfAssignmentExpression()
            throws RecognitionException {
        TSDLParser.ctfAssignmentExpression_return retval = new TSDLParser.ctfAssignmentExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token assignment = null;
        Token type_assignment = null;
        TSDLParser.unaryExpression_return left = null;

        TSDLParser.unaryExpression_return right1 = null;

        TSDLParser.typeSpecifier_return right2 = null;

        TSDLParser.declarationSpecifiers_return declarationSpecifiers178 = null;

        TSDLParser.declaratorList_return declaratorList179 = null;

        TSDLParser.typealiasDecl_return typealiasDecl180 = null;

        CommonTree assignment_tree = null;
        CommonTree type_assignment_tree = null;
        RewriteRuleTokenStream stream_ASSIGNMENT = new RewriteRuleTokenStream(
                adaptor, "token ASSIGNMENT");
        RewriteRuleTokenStream stream_TYPE_ASSIGNMENT = new RewriteRuleTokenStream(
                adaptor, "token TYPE_ASSIGNMENT");
        RewriteRuleSubtreeStream stream_declaratorList = new RewriteRuleSubtreeStream(
                adaptor, "rule declaratorList");
        RewriteRuleSubtreeStream stream_unaryExpression = new RewriteRuleSubtreeStream(
                adaptor, "rule unaryExpression");
        RewriteRuleSubtreeStream stream_declarationSpecifiers = new RewriteRuleSubtreeStream(
                adaptor, "rule declarationSpecifiers");
        RewriteRuleSubtreeStream stream_typeSpecifier = new RewriteRuleSubtreeStream(
                adaptor, "rule typeSpecifier");

        enter("ctfAssignmentExpression");

        try {
            // Grammar/CTFParser.g:987:1: ( (left= unaryExpression (
            // (assignment= ASSIGNMENT right1= unaryExpression ) -> ^(
            // CTF_EXPRESSION_VAL ^( CTF_LEFT $left) ^( CTF_RIGHT $right1) ) |
            // (type_assignment= TYPE_ASSIGNMENT right2= typeSpecifier ) -> ^(
            // CTF_EXPRESSION_TYPE ^( CTF_LEFT $left) ^( CTF_RIGHT ^(
            // TYPE_SPECIFIER_LIST $right2) ) ) ) ) | ( declarationSpecifiers
            // {...}? declaratorList ) -> ^( TYPEDEF declaratorList
            // declarationSpecifiers ) | typealiasDecl )
            int alt59 = 3;
            switch (input.LA(1)) {
            case IDENTIFIER: {
                int LA59_1 = input.LA(2);

                if ((((LA59_1 >= CONSTTOK) && (LA59_1 <= ENUMTOK))
                        || ((LA59_1 >= FLOATINGPOINTTOK) && (LA59_1 <= SIGNEDTOK))
                        || ((LA59_1 >= STRINGTOK) && (LA59_1 <= STRUCTTOK))
                        || ((LA59_1 >= TYPEDEFTOK) && (LA59_1 <= IMAGINARYTOK))
                        || (LA59_1 == POINTER) || (LA59_1 == IDENTIFIER))
                        && ((inTypealiasAlias() || isTypeName(input.LT(1)
                                .getText())))) {
                    alt59 = 2;
                } else if ((((LA59_1 >= ASSIGNMENT) && (LA59_1 <= TYPE_ASSIGNMENT))
                        || (LA59_1 == OPENBRAC) || ((LA59_1 >= ARROW) && (LA59_1 <= DOT)))) {
                    alt59 = 1;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            59, 1, input);

                    throw nvae;
                }
            }
                break;
            case ALIGNTOK:
            case EVENTTOK:
            case STREAMTOK:
            case TRACETOK:
            case ENVTOK:
            case CLOCKTOK:
            case SIGN:
            case OCTAL_LITERAL:
            case DECIMAL_LITERAL:
            case HEX_LITERAL:
            case CHARACTER_LITERAL:
            case STRING_LITERAL: {
                alt59 = 1;
            }
                break;
            case SIGNEDTOK: {
                switch (input.LA(2)) {
                case ASSIGNMENT:
                case TYPE_ASSIGNMENT:
                case OPENBRAC:
                case ARROW:
                case DOT: {
                    alt59 = 1;
                }
                    break;
                case CONSTTOK:
                case CHARTOK:
                case DOUBLETOK:
                case ENUMTOK:
                case FLOATINGPOINTTOK:
                case FLOATTOK:
                case INTEGERTOK:
                case INTTOK:
                case LONGTOK:
                case SHORTTOK:
                case SIGNEDTOK:
                case STRINGTOK:
                case STRUCTTOK:
                case UNSIGNEDTOK:
                case VARIANTTOK:
                case VOIDTOK:
                case BOOLTOK:
                case COMPLEXTOK:
                case IMAGINARYTOK:
                case POINTER:
                case IDENTIFIER: {
                    alt59 = 2;
                }
                    break;
                case TYPEDEFTOK: {
                    alt59 = 2;
                }
                    break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            59, 3, input);

                    throw nvae;
                }

            }
                break;
            case CONSTTOK:
            case CHARTOK:
            case DOUBLETOK:
            case ENUMTOK:
            case FLOATINGPOINTTOK:
            case FLOATTOK:
            case INTEGERTOK:
            case INTTOK:
            case LONGTOK:
            case SHORTTOK:
            case STRUCTTOK:
            case TYPEDEFTOK:
            case UNSIGNEDTOK:
            case VARIANTTOK:
            case VOIDTOK:
            case BOOLTOK:
            case COMPLEXTOK:
            case IMAGINARYTOK: {
                alt59 = 2;
            }
                break;
            case STRINGTOK: {
                switch (input.LA(2)) {
                case ASSIGNMENT:
                case TYPE_ASSIGNMENT:
                case OPENBRAC:
                case ARROW:
                case DOT: {
                    alt59 = 1;
                }
                    break;
                case CONSTTOK:
                case CHARTOK:
                case DOUBLETOK:
                case ENUMTOK:
                case FLOATINGPOINTTOK:
                case FLOATTOK:
                case INTEGERTOK:
                case INTTOK:
                case LONGTOK:
                case SHORTTOK:
                case SIGNEDTOK:
                case STRINGTOK:
                case STRUCTTOK:
                case UNSIGNEDTOK:
                case VARIANTTOK:
                case VOIDTOK:
                case BOOLTOK:
                case COMPLEXTOK:
                case IMAGINARYTOK:
                case LCURL:
                case POINTER:
                case IDENTIFIER: {
                    alt59 = 2;
                }
                    break;
                case TYPEDEFTOK: {
                    alt59 = 2;
                }
                    break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("",
                            59, 5, input);

                    throw nvae;
                }

            }
                break;
            case TYPEALIASTOK: {
                alt59 = 3;
            }
                break;
            default:
                if (state.backtracking > 0) {
                    state.failed = true;
                    return retval;
                }
                NoViableAltException nvae = new NoViableAltException("", 59, 0,
                        input);

                throw nvae;
            }

            switch (alt59) {
            case 1:
            // Grammar/CTFParser.g:988:3: (left= unaryExpression ( (assignment=
            // ASSIGNMENT right1= unaryExpression ) -> ^( CTF_EXPRESSION_VAL ^(
            // CTF_LEFT $left) ^( CTF_RIGHT $right1) ) | (type_assignment=
            // TYPE_ASSIGNMENT right2= typeSpecifier ) -> ^( CTF_EXPRESSION_TYPE
            // ^( CTF_LEFT $left) ^( CTF_RIGHT ^( TYPE_SPECIFIER_LIST $right2) )
            // ) ) )
            {
                // Grammar/CTFParser.g:988:3: (left= unaryExpression (
                // (assignment= ASSIGNMENT right1= unaryExpression ) -> ^(
                // CTF_EXPRESSION_VAL ^( CTF_LEFT $left) ^( CTF_RIGHT $right1) )
                // | (type_assignment= TYPE_ASSIGNMENT right2= typeSpecifier )
                // -> ^( CTF_EXPRESSION_TYPE ^( CTF_LEFT $left) ^( CTF_RIGHT ^(
                // TYPE_SPECIFIER_LIST $right2) ) ) ) )
                // Grammar/CTFParser.g:989:5: left= unaryExpression (
                // (assignment= ASSIGNMENT right1= unaryExpression ) -> ^(
                // CTF_EXPRESSION_VAL ^( CTF_LEFT $left) ^( CTF_RIGHT $right1) )
                // | (type_assignment= TYPE_ASSIGNMENT right2= typeSpecifier )
                // -> ^( CTF_EXPRESSION_TYPE ^( CTF_LEFT $left) ^( CTF_RIGHT ^(
                // TYPE_SPECIFIER_LIST $right2) ) ) )
                {
                    pushFollow(FOLLOW_unaryExpression_in_ctfAssignmentExpression3381);
                    left = unaryExpression();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_unaryExpression.add(left.getTree());
                    }
                    // Grammar/CTFParser.g:990:5: ( (assignment= ASSIGNMENT
                    // right1= unaryExpression ) -> ^( CTF_EXPRESSION_VAL ^(
                    // CTF_LEFT $left) ^( CTF_RIGHT $right1) ) |
                    // (type_assignment= TYPE_ASSIGNMENT right2= typeSpecifier )
                    // -> ^( CTF_EXPRESSION_TYPE ^( CTF_LEFT $left) ^( CTF_RIGHT
                    // ^( TYPE_SPECIFIER_LIST $right2) ) ) )
                    int alt58 = 2;
                    int LA58_0 = input.LA(1);

                    if ((LA58_0 == ASSIGNMENT)) {
                        alt58 = 1;
                    } else if ((LA58_0 == TYPE_ASSIGNMENT)) {
                        alt58 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae = new NoViableAltException(
                                "", 58, 0, input);

                        throw nvae;
                    }
                    switch (alt58) {
                    case 1:
                    // Grammar/CTFParser.g:991:9: (assignment= ASSIGNMENT
                    // right1= unaryExpression )
                    {
                        // Grammar/CTFParser.g:991:9: (assignment= ASSIGNMENT
                        // right1= unaryExpression )
                        // Grammar/CTFParser.g:991:10: assignment= ASSIGNMENT
                        // right1= unaryExpression
                        {
                            assignment = (Token) match(input, ASSIGNMENT,
                                    FOLLOW_ASSIGNMENT_in_ctfAssignmentExpression3400);
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_ASSIGNMENT.add(assignment);
                            }

                            pushFollow(FOLLOW_unaryExpression_in_ctfAssignmentExpression3404);
                            right1 = unaryExpression();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_unaryExpression.add(right1.getTree());
                            }

                        }

                        // AST REWRITE
                        // elements: right1, left
                        // token labels:
                        // rule labels: retval, left, right1
                        // token list labels:
                        // rule list labels:
                        // wildcard labels:
                        if (state.backtracking == 0) {
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
                                    adaptor, "rule retval",
                                    retval != null ? retval.tree : null);
                            RewriteRuleSubtreeStream stream_left = new RewriteRuleSubtreeStream(
                                    adaptor, "rule left",
                                    left != null ? left.tree : null);
                            RewriteRuleSubtreeStream stream_right1 = new RewriteRuleSubtreeStream(
                                    adaptor, "rule right1",
                                    right1 != null ? right1.tree : null);

                            root_0 = (CommonTree) adaptor.nil();
                            // 991:56: -> ^( CTF_EXPRESSION_VAL ^( CTF_LEFT
                            // $left) ^( CTF_RIGHT $right1) )
                            {
                                // Grammar/CTFParser.g:991:59: ^(
                                // CTF_EXPRESSION_VAL ^( CTF_LEFT $left) ^(
                                // CTF_RIGHT $right1) )
                                {
                                    CommonTree root_1 = (CommonTree) adaptor
                                            .nil();
                                    root_1 = (CommonTree) adaptor.becomeRoot(
                                            adaptor.create(CTF_EXPRESSION_VAL,
                                                    "CTF_EXPRESSION_VAL"),
                                            root_1);

                                    // Grammar/CTFParser.g:991:80: ^( CTF_LEFT
                                    // $left)
                                    {
                                        CommonTree root_2 = (CommonTree) adaptor
                                                .nil();
                                        root_2 = (CommonTree) adaptor
                                                .becomeRoot(adaptor.create(
                                                        CTF_LEFT, "CTF_LEFT"),
                                                        root_2);

                                        adaptor.addChild(root_2,
                                                stream_left.nextTree());

                                        adaptor.addChild(root_1, root_2);
                                    }
                                    // Grammar/CTFParser.g:991:98: ^( CTF_RIGHT
                                    // $right1)
                                    {
                                        CommonTree root_2 = (CommonTree) adaptor
                                                .nil();
                                        root_2 = (CommonTree) adaptor
                                                .becomeRoot(
                                                        adaptor.create(
                                                                CTF_RIGHT,
                                                                "CTF_RIGHT"),
                                                        root_2);

                                        adaptor.addChild(root_2,
                                                stream_right1.nextTree());

                                        adaptor.addChild(root_1, root_2);
                                    }

                                    adaptor.addChild(root_0, root_1);
                                }

                            }

                            retval.tree = root_0;
                        }
                    }
                        break;
                    case 2:
                    // Grammar/CTFParser.g:992:9: (type_assignment=
                    // TYPE_ASSIGNMENT right2= typeSpecifier )
                    {
                        // Grammar/CTFParser.g:992:9: (type_assignment=
                        // TYPE_ASSIGNMENT right2= typeSpecifier )
                        // Grammar/CTFParser.g:992:10: type_assignment=
                        // TYPE_ASSIGNMENT right2= typeSpecifier
                        {
                            type_assignment = (Token) match(input,
                                    TYPE_ASSIGNMENT,
                                    FOLLOW_TYPE_ASSIGNMENT_in_ctfAssignmentExpression3438);
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_TYPE_ASSIGNMENT.add(type_assignment);
                            }

                            pushFollow(FOLLOW_typeSpecifier_in_ctfAssignmentExpression3443);
                            right2 = typeSpecifier();

                            state._fsp--;
                            if (state.failed) {
                                return retval;
                            }
                            if (state.backtracking == 0) {
                                stream_typeSpecifier.add(right2.getTree());
                            }

                        }

                        // AST REWRITE
                        // elements: left, right2
                        // token labels:
                        // rule labels: retval, left, right2
                        // token list labels:
                        // rule list labels:
                        // wildcard labels:
                        if (state.backtracking == 0) {
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
                                    adaptor, "rule retval",
                                    retval != null ? retval.tree : null);
                            RewriteRuleSubtreeStream stream_left = new RewriteRuleSubtreeStream(
                                    adaptor, "rule left",
                                    left != null ? left.tree : null);
                            RewriteRuleSubtreeStream stream_right2 = new RewriteRuleSubtreeStream(
                                    adaptor, "rule right2",
                                    right2 != null ? right2.tree : null);

                            root_0 = (CommonTree) adaptor.nil();
                            // 992:65: -> ^( CTF_EXPRESSION_TYPE ^( CTF_LEFT
                            // $left) ^( CTF_RIGHT ^( TYPE_SPECIFIER_LIST
                            // $right2) ) )
                            {
                                // Grammar/CTFParser.g:992:68: ^(
                                // CTF_EXPRESSION_TYPE ^( CTF_LEFT $left) ^(
                                // CTF_RIGHT ^( TYPE_SPECIFIER_LIST $right2) ) )
                                {
                                    CommonTree root_1 = (CommonTree) adaptor
                                            .nil();
                                    root_1 = (CommonTree) adaptor.becomeRoot(
                                            adaptor.create(CTF_EXPRESSION_TYPE,
                                                    "CTF_EXPRESSION_TYPE"),
                                            root_1);

                                    // Grammar/CTFParser.g:992:90: ^( CTF_LEFT
                                    // $left)
                                    {
                                        CommonTree root_2 = (CommonTree) adaptor
                                                .nil();
                                        root_2 = (CommonTree) adaptor
                                                .becomeRoot(adaptor.create(
                                                        CTF_LEFT, "CTF_LEFT"),
                                                        root_2);

                                        adaptor.addChild(root_2,
                                                stream_left.nextTree());

                                        adaptor.addChild(root_1, root_2);
                                    }
                                    // Grammar/CTFParser.g:992:108: ^( CTF_RIGHT
                                    // ^( TYPE_SPECIFIER_LIST $right2) )
                                    {
                                        CommonTree root_2 = (CommonTree) adaptor
                                                .nil();
                                        root_2 = (CommonTree) adaptor
                                                .becomeRoot(
                                                        adaptor.create(
                                                                CTF_RIGHT,
                                                                "CTF_RIGHT"),
                                                        root_2);

                                        // Grammar/CTFParser.g:992:120: ^(
                                        // TYPE_SPECIFIER_LIST $right2)
                                        {
                                            CommonTree root_3 = (CommonTree) adaptor
                                                    .nil();
                                            root_3 = (CommonTree) adaptor
                                                    .becomeRoot(
                                                            adaptor.create(
                                                                    TYPE_SPECIFIER_LIST,
                                                                    "TYPE_SPECIFIER_LIST"),
                                                            root_3);

                                            adaptor.addChild(root_3,
                                                    stream_right2.nextTree());

                                            adaptor.addChild(root_2, root_3);
                                        }

                                        adaptor.addChild(root_1, root_2);
                                    }

                                    adaptor.addChild(root_0, root_1);
                                }

                            }

                            retval.tree = root_0;
                        }
                    }
                        break;

                    }

                }

            }
                break;
            case 2:
            // Grammar/CTFParser.g:998:5: ( declarationSpecifiers {...}?
            // declaratorList )
            {
                // Grammar/CTFParser.g:998:5: ( declarationSpecifiers {...}?
                // declaratorList )
                // Grammar/CTFParser.g:998:6: declarationSpecifiers {...}?
                // declaratorList
                {
                    pushFollow(FOLLOW_declarationSpecifiers_in_ctfAssignmentExpression3493);
                    declarationSpecifiers178 = declarationSpecifiers();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_declarationSpecifiers
                                .add(declarationSpecifiers178.getTree());
                    }
                    if (!((inTypedef()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return retval;
                        }
                        throw new FailedPredicateException(input,
                                "ctfAssignmentExpression", "inTypedef()");
                    }
                    pushFollow(FOLLOW_declaratorList_in_ctfAssignmentExpression3497);
                    declaratorList179 = declaratorList();

                    state._fsp--;
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 0) {
                        stream_declaratorList.add(declaratorList179.getTree());
                    }

                }

                // AST REWRITE
                // elements: declaratorList, declarationSpecifiers
                // token labels:
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(
                            adaptor, "rule retval",
                            retval != null ? retval.tree : null);

                    root_0 = (CommonTree) adaptor.nil();
                    // 999:5: -> ^( TYPEDEF declaratorList declarationSpecifiers
                    // )
                    {
                        // Grammar/CTFParser.g:999:8: ^( TYPEDEF declaratorList
                        // declarationSpecifiers )
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot(
                                    adaptor.create(TYPEDEF, "TYPEDEF"), root_1);

                            adaptor.addChild(root_1,
                                    stream_declaratorList.nextTree());
                            adaptor.addChild(root_1,
                                    stream_declarationSpecifiers.nextTree());

                            adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                }
            }
                break;
            case 3:
            // Grammar/CTFParser.g:1002:5: typealiasDecl
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_typealiasDecl_in_ctfAssignmentExpression3525);
                typealiasDecl180 = typealiasDecl();

                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 0) {
                    adaptor.addChild(root_0, typealiasDecl180.getTree());
                }

            }
                break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0) {

                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start,
                        retval.stop);
            }
            if (state.backtracking == 0) {

                if (inTypedef()) {
                    typedefOff();
                }
                exit("ctfAssignmentExpression");

            }
        }

        catch (RecognitionException e) {
            throw e;

        }
        return retval;
    }

    // $ANTLR end "ctfAssignmentExpression"

    // $ANTLR start synpred1_CTFParser
    public final void synpred1_CTFParser_fragment() throws RecognitionException {
        // Grammar/CTFParser.g:250:5: ( IDENTIFIER )
        // Grammar/CTFParser.g:250:6: IDENTIFIER
        {
            match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_synpred1_CTFParser470);
            if (state.failed) {
                return;
            }

        }
    }

    // $ANTLR end synpred1_CTFParser

    // $ANTLR start synpred2_CTFParser
    public final void synpred2_CTFParser_fragment() throws RecognitionException {
        // Grammar/CTFParser.g:251:5: ( ctfKeyword )
        // Grammar/CTFParser.g:251:6: ctfKeyword
        {
            pushFollow(FOLLOW_ctfKeyword_in_synpred2_CTFParser492);
            ctfKeyword();

            state._fsp--;
            if (state.failed) {
                return;
            }

        }
    }

    // $ANTLR end synpred2_CTFParser

    // $ANTLR start synpred3_CTFParser
    public final void synpred3_CTFParser_fragment() throws RecognitionException {
        // Grammar/CTFParser.g:252:5: ( STRING_LITERAL )
        // Grammar/CTFParser.g:252:6: STRING_LITERAL
        {
            match(input, STRING_LITERAL,
                    FOLLOW_STRING_LITERAL_in_synpred3_CTFParser512);
            if (state.failed) {
                return;
            }

        }
    }

    // $ANTLR end synpred3_CTFParser

    // Delegated rules

    public final boolean synpred2_CTFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_CTFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            if(System.err != null) {//findbugs
                System.err.println("impossible: " + re);
            }
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred1_CTFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_CTFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            if( System.err != null ) {//findbugs
                System.err.println("impossible: " + re);
            }
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred3_CTFParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_CTFParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            if( System.err != null ) { //findbugs
                System.err.println("impossible: " + re);
            }
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    protected DFA10 dfa10 = new DFA10(this);
    protected DFA17 dfa17 = new DFA17(this);
    protected DFA24 dfa24 = new DFA24(this);
    protected DFA32 dfa32 = new DFA32(this);
    protected DFA54 dfa54 = new DFA54(this);
    static final String DFA10_eotS = "\16\uffff";
    static final String DFA10_eofS = "\16\uffff";
    static final String DFA10_minS = "\1\4\15\uffff";
    static final String DFA10_maxS = "\1\116\15\uffff";
    static final String DFA10_acceptS = "\1\uffff\2\1\7\uffff\1\2\3\uffff";
    static final String DFA10_specialS = "\16\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1\4\uffff\1\2\6\uffff\1\1\1\12\1\1\1\uffff\1\12\10\uffff"
                    + "\2\12\22\uffff\1\1\4\uffff\1\1\1\uffff\1\1\2\uffff\1\1\10\uffff"
                    + "\1\1\2\uffff\1\1\6\uffff\1\1", "", "", "", "", "", "",
            "", "", "", "", "", "", "" };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA
            .unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA
            .unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA
            .unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    static class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }

        @Override
        public String getDescription() {
            return "281:1: postfixExpression : ( ( primaryExpression ) ( postfixExpressionSuffix )* | ( ( ctfSpecifierHead ) ( postfixExpressionSuffix )+ ) );";
        }
    }

    static final String DFA17_eotS = "\22\uffff";
    static final String DFA17_eofS = "\22\uffff";
    static final String DFA17_minS = "\1\6\21\uffff";
    static final String DFA17_maxS = "\1\116\21\uffff";
    static final String DFA17_acceptS = "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"
            + "\15\1\16\1\17\1\20\1\21";
    static final String DFA17_specialS = "\1\0\21\uffff}>";
    static final String[] DFA17_transitionS = {
            "\1\7\1\10\1\17\1\uffff\1\20\1\1\1\20\1\2\1\3\1\4\1\5\1\uffff"
                    + "\1\20\1\15\3\uffff\1\6\1\16\1\11\1\12\1\13\1\14\61\uffff\1\21",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };

    static final short[] DFA17_eot = DFA.unpackEncodedString(DFA17_eotS);
    static final short[] DFA17_eof = DFA.unpackEncodedString(DFA17_eofS);
    static final char[] DFA17_min = DFA
            .unpackEncodedStringToUnsignedChars(DFA17_minS);
    static final char[] DFA17_max = DFA
            .unpackEncodedStringToUnsignedChars(DFA17_maxS);
    static final short[] DFA17_accept = DFA.unpackEncodedString(DFA17_acceptS);
    static final short[] DFA17_special = DFA
            .unpackEncodedString(DFA17_specialS);
    static final short[][] DFA17_transition;

    static {
        int numStates = DFA17_transitionS.length;
        DFA17_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA17_transition[i] = DFA.unpackEncodedString(DFA17_transitionS[i]);
        }
    }

    class DFA17 extends DFA {

        public DFA17(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 17;
            this.eot = DFA17_eot;
            this.eof = DFA17_eof;
            this.min = DFA17_min;
            this.max = DFA17_max;
            this.accept = DFA17_accept;
            this.special = DFA17_special;
            this.transition = DFA17_transition;
        }

        @Override
        public String getDescription() {
            return "384:1: typeSpecifier : ( FLOATTOK | INTTOK | LONGTOK | SHORTTOK | SIGNEDTOK | UNSIGNEDTOK | CHARTOK | DOUBLETOK | VOIDTOK | BOOLTOK | COMPLEXTOK | IMAGINARYTOK | structSpecifier | variantSpecifier | enumSpecifier | ctfTypeSpecifier | {...}? => typedefName );";
        }

        @SuppressWarnings({ "synthetic-access" })
        @Override
        public int specialStateTransition(final int inputS, IntStream _input)
                throws NoViableAltException {
            TokenStream input = (TokenStream) _input;
            int _s = inputS;
            int s = inputS;
            switch (s) {
            case 0:
                int LA17_0 = input.LA(1);

                int index17_0 = input.index();
                input.rewind();
                s = -1;
                if ((LA17_0 == FLOATTOK)) {
                    s = 1;
                }

                else if ((LA17_0 == INTTOK)) {
                    s = 2;
                }

                else if ((LA17_0 == LONGTOK)) {
                    s = 3;
                }

                else if ((LA17_0 == SHORTTOK)) {
                    s = 4;
                }

                else if ((LA17_0 == SIGNEDTOK)) {
                    s = 5;
                }

                else if ((LA17_0 == UNSIGNEDTOK)) {
                    s = 6;
                }

                else if ((LA17_0 == CHARTOK)) {
                    s = 7;
                }

                else if ((LA17_0 == DOUBLETOK)) {
                    s = 8;
                }

                else if ((LA17_0 == VOIDTOK)) {
                    s = 9;
                }

                else if ((LA17_0 == BOOLTOK)) {
                    s = 10;
                }

                else if ((LA17_0 == COMPLEXTOK)) {
                    s = 11;
                }

                else if ((LA17_0 == IMAGINARYTOK)) {
                    s = 12;
                }

                else if ((LA17_0 == STRUCTTOK)) {
                    s = 13;
                }

                else if ((LA17_0 == VARIANTTOK)) {
                    s = 14;
                }

                else if ((LA17_0 == ENUMTOK)) {
                    s = 15;
                }

                else if (((LA17_0 == FLOATINGPOINTTOK)
                        || (LA17_0 == INTEGERTOK) || (LA17_0 == STRINGTOK))) {
                    s = 16;
                }

                else if ((LA17_0 == IDENTIFIER)
                        && ((inTypealiasAlias() || isTypeName(input.LT(1)
                                .getText())))) {
                    s = 17;
                }

                input.seek(index17_0);
                if (s >= 0) {
                    return s;
                }
                break;
            }
            if (state.backtracking > 0) {
                state.failed = true;
                return -1;
            }
            NoViableAltException nvae = new NoViableAltException(
                    getDescription(), 17, _s, input);
            error(nvae);
            throw nvae;
        }
    }

    static final String DFA24_eotS = "\6\uffff";
    static final String DFA24_eofS = "\6\uffff";
    static final String DFA24_minS = "\1\60\1\5\1\0\1\5\2\uffff";
    static final String DFA24_maxS = "\2\116\1\0\1\116\2\uffff";
    static final String DFA24_acceptS = "\4\uffff\1\1\1\2";
    static final String DFA24_specialS = "\2\uffff\1\0\3\uffff}>";
    static final String[] DFA24_transitionS = { "\1\1\35\uffff\1\2",
            "\1\3\52\uffff\1\1\35\uffff\1\2", "\1\uffff",
            "\1\3\52\uffff\1\1\35\uffff\1\2", "", "" };

    static final short[] DFA24_eot = DFA.unpackEncodedString(DFA24_eotS);
    static final short[] DFA24_eof = DFA.unpackEncodedString(DFA24_eofS);
    static final char[] DFA24_min = DFA
            .unpackEncodedStringToUnsignedChars(DFA24_minS);
    static final char[] DFA24_max = DFA
            .unpackEncodedStringToUnsignedChars(DFA24_maxS);
    static final short[] DFA24_accept = DFA.unpackEncodedString(DFA24_acceptS);
    static final short[] DFA24_special = DFA
            .unpackEncodedString(DFA24_specialS);
    static final short[][] DFA24_transition;

    static {
        int numStates = DFA24_transitionS.length;
        DFA24_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA24_transition[i] = DFA.unpackEncodedString(DFA24_transitionS[i]);
        }
    }

    class DFA24 extends DFA {

        public DFA24(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 24;
            this.eot = DFA24_eot;
            this.eof = DFA24_eof;
            this.min = DFA24_min;
            this.max = DFA24_max;
            this.accept = DFA24_accept;
            this.special = DFA24_special;
            this.transition = DFA24_transition;
        }

        @Override
        public String getDescription() {
            return "521:7: ({...}? => declaratorList -> ^( TYPEDEF declaratorList declarationSpecifiers ) | structOrVariantDeclaratorList -> ^( SV_DECLARATION declarationSpecifiers structOrVariantDeclaratorList ) )";
        }

        @Override
        public int specialStateTransition(int s, IntStream _input)
                throws NoViableAltException {
            TokenStream input = (TokenStream) _input;
            int _s = s;
            switch (s) {
            case 0:
                int LA24_2 = input.LA(1);

                int index24_2 = input.index();
                input.rewind();
                s = -1;
                if (((inTypedef()))) {
                    s = 4;
                }

                else if ((true)) {
                    s = 5;
                }

                input.seek(index24_2);
                if (s >= 0) {
                    return s;
                }
                break;
            }
            if (state.backtracking > 0) {
                state.failed = true;
                return -1;
            }
            NoViableAltException nvae = new NoViableAltException(
                    getDescription(), 24, _s, input);
            error(nvae);
            throw nvae;
        }
    }

    static final String DFA32_eotS = "\35\uffff";
    static final String DFA32_eofS = "\1\3\34\uffff";
    static final String DFA32_minS = "\1\5\34\uffff";
    static final String DFA32_maxS = "\1\116\34\uffff";
    static final String DFA32_acceptS = "\1\uffff\1\1\1\2\1\3\31\uffff";
    static final String DFA32_specialS = "\35\uffff}>";
    static final String[] DFA32_transitionS = {
            "\4\3\1\uffff\7\3\1\uffff\2\3\2\uffff\7\3\6\uffff\1\1\2\uffff"
                    + "\1\3\4\uffff\1\3\1\uffff\1\2\1\uffff\2\3\35\uffff\1\3",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "" };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA
            .unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA
            .unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA
            .unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    static class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }

        @Override
        public String getDescription() {
            return "655:4: ( enumContainerType enumBody | enumBody | )";
        }
    }

    static final String DFA54_eotS = "\34\uffff";
    static final String DFA54_eofS = "\1\2\33\uffff";
    static final String DFA54_minS = "\1\5\33\uffff";
    static final String DFA54_maxS = "\1\116\33\uffff";
    static final String DFA54_acceptS = "\1\uffff\1\1\1\2\31\uffff";
    static final String DFA54_specialS = "\34\uffff}>";
    static final String[] DFA54_transitionS = {
            "\4\2\1\uffff\7\2\1\uffff\2\2\2\uffff\7\2\11\uffff\1\2\4\uffff"
                    + "\1\2\1\uffff\1\1\1\uffff\2\2\35\uffff\1\2", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "" };

    static final short[] DFA54_eot = DFA.unpackEncodedString(DFA54_eotS);
    static final short[] DFA54_eof = DFA.unpackEncodedString(DFA54_eofS);
    static final char[] DFA54_min = DFA
            .unpackEncodedStringToUnsignedChars(DFA54_minS);
    static final char[] DFA54_max = DFA
            .unpackEncodedStringToUnsignedChars(DFA54_maxS);
    static final short[] DFA54_accept = DFA.unpackEncodedString(DFA54_acceptS);
    static final short[] DFA54_special = DFA
            .unpackEncodedString(DFA54_specialS);
    static final short[][] DFA54_transition;

    static {
        int numStates = DFA54_transitionS.length;
        DFA54_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA54_transition[i] = DFA.unpackEncodedString(DFA54_transitionS[i]);
        }
    }

    static class DFA54 extends DFA {

        public DFA54(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 54;
            this.eot = DFA54_eot;
            this.eof = DFA54_eof;
            this.min = DFA54_min;
            this.max = DFA54_max;
            this.accept = DFA54_accept;
            this.special = DFA54_special;
            this.transition = DFA54_transition;
        }

        @Override
        public String getDescription() {
            return "954:15: ( ctfBody )?";
        }
    }

    public static final BitSet FOLLOW_declaration_in_parse321 = new BitSet(
            new long[] { 0x000000007FFFFFE0L, 0x0000000000004000L });
    public static final BitSet FOLLOW_EOF_in_parse324 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SIGN_in_numberLiteral357 = new BitSet(
            new long[] { 0x0942000000000000L });
    public static final BitSet FOLLOW_HEX_LITERAL_in_numberLiteral362 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_DECIMAL_LITERAL_in_numberLiteral379 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OCTAL_LITERAL_in_numberLiteral396 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_numberLiteral_in_constant432 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enumConstant_in_constant438 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_CHARACTER_LITERAL_in_constant444 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_primaryExpression475 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ctfKeyword_in_primaryExpression497 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_primaryExpression517 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_constant_in_primaryExpression538 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_DOT_in_reference564 = new BitSet(
            new long[] { 0x0000000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_ARROW_in_reference570 = new BitSet(
            new long[] { 0x0000000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_IDENTIFIER_in_reference573 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OPENBRAC_in_postfixExpressionSuffix612 = new BitSet(
            new long[] { 0x0942000060170210L, 0x0000000000004090L });
    public static final BitSet FOLLOW_unaryExpression_in_postfixExpressionSuffix614 = new BitSet(
            new long[] { 0x0000040000000000L });
    public static final BitSet FOLLOW_CLOSEBRAC_in_postfixExpressionSuffix616 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_reference_in_postfixExpressionSuffix624 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression648 = new BitSet(
            new long[] { 0x000C020000000002L });
    public static final BitSet FOLLOW_postfixExpressionSuffix_in_postfixExpression652 = new BitSet(
            new long[] { 0x000C020000000002L });
    public static final BitSet FOLLOW_ctfSpecifierHead_in_postfixExpression661 = new BitSet(
            new long[] { 0x000C020000000000L });
    public static final BitSet FOLLOW_postfixExpressionSuffix_in_postfixExpression666 = new BitSet(
            new long[] { 0x000C020000000002L });
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression700 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_enumConstant725 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_enumConstant741 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ctfKeyword_in_enumConstant757 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_declarationSpecifiers_in_declaration790 = new BitSet(
            new long[] { 0x0001800000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_declaratorList_in_declaration792 = new BitSet(
            new long[] { 0x0000800000000000L });
    public static final BitSet FOLLOW_TERM_in_declaration795 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ctfSpecifier_in_declaration844 = new BitSet(
            new long[] { 0x0000800000000000L });
    public static final BitSet FOLLOW_TERM_in_declaration846 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_storageClassSpecifier_in_declarationSpecifiers891 = new BitSet(
            new long[] { 0x000000001FCDFDE2L, 0x0000000000004000L });
    public static final BitSet FOLLOW_typeQualifier_in_declarationSpecifiers898 = new BitSet(
            new long[] { 0x000000001FCDFDE2L, 0x0000000000004000L });
    public static final BitSet FOLLOW_typeSpecifier_in_declarationSpecifiers905 = new BitSet(
            new long[] { 0x000000001FCDFDE2L, 0x0000000000004000L });
    public static final BitSet FOLLOW_declarator_in_declaratorList946 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_SEPARATOR_in_declaratorList949 = new BitSet(
            new long[] { 0x0001000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_declarator_in_declaratorList951 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_abstractDeclarator_in_abstractDeclaratorList985 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_SEPARATOR_in_abstractDeclaratorList988 = new BitSet(
            new long[] { 0x0001080000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_abstractDeclarator_in_abstractDeclaratorList990 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_TYPEDEFTOK_in_storageClassSpecifier1014 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FLOATTOK_in_typeSpecifier1040 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_INTTOK_in_typeSpecifier1046 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LONGTOK_in_typeSpecifier1052 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SHORTTOK_in_typeSpecifier1058 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SIGNEDTOK_in_typeSpecifier1064 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_UNSIGNEDTOK_in_typeSpecifier1070 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_CHARTOK_in_typeSpecifier1076 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_DOUBLETOK_in_typeSpecifier1082 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_VOIDTOK_in_typeSpecifier1088 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_BOOLTOK_in_typeSpecifier1094 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_COMPLEXTOK_in_typeSpecifier1100 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IMAGINARYTOK_in_typeSpecifier1106 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_structSpecifier_in_typeSpecifier1112 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_variantSpecifier_in_typeSpecifier1118 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enumSpecifier_in_typeSpecifier1124 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ctfTypeSpecifier_in_typeSpecifier1130 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_typedefName_in_typeSpecifier1140 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_CONSTTOK_in_typeQualifier1163 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ALIGNTOK_in_alignAttribute1176 = new BitSet(
            new long[] { 0x0000080000000000L });
    public static final BitSet FOLLOW_LPAREN_in_alignAttribute1178 = new BitSet(
            new long[] { 0x0942000060170210L, 0x0000000000004090L });
    public static final BitSet FOLLOW_unaryExpression_in_alignAttribute1180 = new BitSet(
            new long[] { 0x0000100000000000L });
    public static final BitSet FOLLOW_RPAREN_in_alignAttribute1182 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LCURL_in_structBody1223 = new BitSet(
            new long[] { 0x000040007FFFFFE0L, 0x0000000000004000L });
    public static final BitSet FOLLOW_structOrVariantDeclarationList_in_structBody1225 = new BitSet(
            new long[] { 0x0000400000000000L });
    public static final BitSet FOLLOW_RCURL_in_structBody1228 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRUCTTOK_in_structSpecifier1266 = new BitSet(
            new long[] { 0x0000200000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_structName_in_structSpecifier1288 = new BitSet(
            new long[] { 0x0000200000000012L, 0x0000000000004000L });
    public static final BitSet FOLLOW_alignAttribute_in_structSpecifier1304 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_structBody_in_structSpecifier1333 = new BitSet(
            new long[] { 0x0000000000000012L });
    public static final BitSet FOLLOW_alignAttribute_in_structSpecifier1358 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_structBody_in_structSpecifier1456 = new BitSet(
            new long[] { 0x0000000000000012L });
    public static final BitSet FOLLOW_alignAttribute_in_structSpecifier1474 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_structName1550 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_structOrVariantDeclaration_in_structOrVariantDeclarationList1581 = new BitSet(
            new long[] { 0x000000007FFFFFE2L, 0x0000000000004000L });
    public static final BitSet FOLLOW_declarationSpecifiers_in_structOrVariantDeclaration1619 = new BitSet(
            new long[] { 0x0001000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_declaratorList_in_structOrVariantDeclaration1651 = new BitSet(
            new long[] { 0x0000800000000000L });
    public static final BitSet FOLLOW_structOrVariantDeclaratorList_in_structOrVariantDeclaration1685 = new BitSet(
            new long[] { 0x0000800000000000L });
    public static final BitSet FOLLOW_typealiasDecl_in_structOrVariantDeclaration1735 = new BitSet(
            new long[] { 0x0000800000000000L });
    public static final BitSet FOLLOW_TERM_in_structOrVariantDeclaration1747 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_typeQualifier_in_specifierQualifierList1771 = new BitSet(
            new long[] { 0x000000001FCDFDE2L, 0x0000000000004000L });
    public static final BitSet FOLLOW_typeSpecifier_in_specifierQualifierList1775 = new BitSet(
            new long[] { 0x000000001FCDFDE2L, 0x0000000000004000L });
    public static final BitSet FOLLOW_structOrVariantDeclarator_in_structOrVariantDeclaratorList1812 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_SEPARATOR_in_structOrVariantDeclaratorList1815 = new BitSet(
            new long[] { 0x0001000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_structOrVariantDeclarator_in_structOrVariantDeclaratorList1817 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_declarator_in_structOrVariantDeclarator1858 = new BitSet(
            new long[] { 0x0000000800000002L });
    public static final BitSet FOLLOW_COLON_in_structOrVariantDeclarator1861 = new BitSet(
            new long[] { 0x0942000000000000L });
    public static final BitSet FOLLOW_numberLiteral_in_structOrVariantDeclarator1863 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_VARIANTTOK_in_variantSpecifier1897 = new BitSet(
            new long[] { 0x0000208000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_variantName_in_variantSpecifier1915 = new BitSet(
            new long[] { 0x0000208000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_variantTag_in_variantSpecifier1946 = new BitSet(
            new long[] { 0x0000208000000002L, 0x0000000000004000L });
    public static final BitSet FOLLOW_variantBody_in_variantSpecifier1972 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_variantBody_in_variantSpecifier2040 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_variantTag_in_variantSpecifier2065 = new BitSet(
            new long[] { 0x0000208000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_variantBody_in_variantSpecifier2067 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_variantBody_in_variantSpecifier2078 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_variantName2120 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LCURL_in_variantBody2156 = new BitSet(
            new long[] { 0x000000007FFFFFE0L, 0x0000000000004000L });
    public static final BitSet FOLLOW_structOrVariantDeclarationList_in_variantBody2158 = new BitSet(
            new long[] { 0x0000400000000000L });
    public static final BitSet FOLLOW_RCURL_in_variantBody2160 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LT_in_variantTag2191 = new BitSet(
            new long[] { 0x0000000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_IDENTIFIER_in_variantTag2193 = new BitSet(
            new long[] { 0x0000010000000000L });
    public static final BitSet FOLLOW_GT_in_variantTag2195 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ENUMTOK_in_enumSpecifier2225 = new BitSet(
            new long[] { 0x0000200800000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_enumName_in_enumSpecifier2240 = new BitSet(
            new long[] { 0x0000200800000002L, 0x0000000000004000L });
    public static final BitSet FOLLOW_enumContainerType_in_enumSpecifier2251 = new BitSet(
            new long[] { 0x0000200800000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_enumBody_in_enumSpecifier2253 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enumBody_in_enumSpecifier2265 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enumContainerType_in_enumSpecifier2300 = new BitSet(
            new long[] { 0x0000200800000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_enumBody_in_enumSpecifier2302 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enumBody_in_enumSpecifier2311 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_enumName2356 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LCURL_in_enumBody2389 = new BitSet(
            new long[] { 0x0000000000050210L, 0x0000000000004080L });
    public static final BitSet FOLLOW_enumeratorList_in_enumBody2391 = new BitSet(
            new long[] { 0x0000400400000000L });
    public static final BitSet FOLLOW_SEPARATOR_in_enumBody2394 = new BitSet(
            new long[] { 0x0000400000000000L });
    public static final BitSet FOLLOW_RCURL_in_enumBody2396 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_RCURL_in_enumBody2400 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_COLON_in_enumContainerType2432 = new BitSet(
            new long[] { 0x000000001FCDFDE0L, 0x0000000000004000L });
    public static final BitSet FOLLOW_declarationSpecifiers_in_enumContainerType2434 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enumerator_in_enumeratorList2465 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_SEPARATOR_in_enumeratorList2468 = new BitSet(
            new long[] { 0x0000000000050210L, 0x0000000000004080L });
    public static final BitSet FOLLOW_enumerator_in_enumeratorList2470 = new BitSet(
            new long[] { 0x0000000400000002L });
    public static final BitSet FOLLOW_enumConstant_in_enumerator2506 = new BitSet(
            new long[] { 0x0000002000000002L });
    public static final BitSet FOLLOW_enumeratorValue_in_enumerator2508 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ASSIGNMENT_in_enumeratorValue2532 = new BitSet(
            new long[] { 0x0942000060170210L, 0x0000000000004090L });
    public static final BitSet FOLLOW_unaryExpression_in_enumeratorValue2536 = new BitSet(
            new long[] { 0x0000001000000002L });
    public static final BitSet FOLLOW_ELIPSES_in_enumeratorValue2562 = new BitSet(
            new long[] { 0x0942000060170210L, 0x0000000000004090L });
    public static final BitSet FOLLOW_unaryExpression_in_enumeratorValue2566 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_pointer_in_declarator2608 = new BitSet(
            new long[] { 0x0001000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_directDeclarator_in_declarator2611 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_directDeclarator2655 = new BitSet(
            new long[] { 0x0000020000000002L });
    public static final BitSet FOLLOW_directDeclaratorSuffix_in_directDeclarator2673 = new BitSet(
            new long[] { 0x0000020000000002L });
    public static final BitSet FOLLOW_OPENBRAC_in_directDeclaratorSuffix2686 = new BitSet(
            new long[] { 0x0942000060170210L, 0x0000000000004090L });
    public static final BitSet FOLLOW_directDeclaratorLength_in_directDeclaratorSuffix2688 = new BitSet(
            new long[] { 0x0000040000000000L });
    public static final BitSet FOLLOW_CLOSEBRAC_in_directDeclaratorSuffix2690 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_unaryExpression_in_directDeclaratorLength2711 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_pointer_in_abstractDeclarator2742 = new BitSet(
            new long[] { 0x0001080000000002L, 0x0000000000004000L });
    public static final BitSet FOLLOW_directAbstractDeclarator_in_abstractDeclarator2745 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_directAbstractDeclarator_in_abstractDeclarator2765 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_directAbstractDeclarator2805 = new BitSet(
            new long[] { 0x0000020000000002L });
    public static final BitSet FOLLOW_LPAREN_in_directAbstractDeclarator2814 = new BitSet(
            new long[] { 0x0001080000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_abstractDeclarator_in_directAbstractDeclarator2816 = new BitSet(
            new long[] { 0x0000100000000000L });
    public static final BitSet FOLLOW_RPAREN_in_directAbstractDeclarator2818 = new BitSet(
            new long[] { 0x0000020000000002L });
    public static final BitSet FOLLOW_OPENBRAC_in_directAbstractDeclarator2833 = new BitSet(
            new long[] { 0x0942040060170210L, 0x0000000000004090L });
    public static final BitSet FOLLOW_unaryExpression_in_directAbstractDeclarator2835 = new BitSet(
            new long[] { 0x0000040000000000L });
    public static final BitSet FOLLOW_CLOSEBRAC_in_directAbstractDeclarator2838 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_POINTER_in_pointer2866 = new BitSet(
            new long[] { 0x0000000000000022L });
    public static final BitSet FOLLOW_typeQualifierList_in_pointer2868 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_typeQualifier_in_typeQualifierList2891 = new BitSet(
            new long[] { 0x0000000000000022L });
    public static final BitSet FOLLOW_IDENTIFIER_in_typedefName2917 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_declarationSpecifiers_in_typealiasTarget2945 = new BitSet(
            new long[] { 0x0001080000000002L, 0x0000000000004000L });
    public static final BitSet FOLLOW_abstractDeclaratorList_in_typealiasTarget2947 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_abstractDeclaratorList_in_typealiasAlias2980 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_declarationSpecifiers_in_typealiasAlias2989 = new BitSet(
            new long[] { 0x0001080000000002L, 0x0000000000004000L });
    public static final BitSet FOLLOW_abstractDeclaratorList_in_typealiasAlias2991 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_TYPEALIASTOK_in_typealiasDecl3023 = new BitSet(
            new long[] { 0x000000001FCDFDE0L, 0x0000000000004000L });
    public static final BitSet FOLLOW_typealiasTarget_in_typealiasDecl3025 = new BitSet(
            new long[] { 0x0000004000000000L });
    public static final BitSet FOLLOW_TYPE_ASSIGNMENT_in_typealiasDecl3027 = new BitSet(
            new long[] { 0x000108001FCDFDE0L, 0x0000000000004000L });
    public static final BitSet FOLLOW_typealiasAlias_in_typealiasDecl3029 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_in_ctfKeyword0 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ctfSpecifierHead_in_ctfSpecifier3123 = new BitSet(
            new long[] { 0x0000200000000000L });
    public static final BitSet FOLLOW_ctfBody_in_ctfSpecifier3125 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_typealiasDecl_in_ctfSpecifier3144 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_EVENTTOK_in_ctfSpecifierHead3176 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STREAMTOK_in_ctfSpecifierHead3185 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_TRACETOK_in_ctfSpecifierHead3194 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ENVTOK_in_ctfSpecifierHead3203 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_CLOCKTOK_in_ctfSpecifierHead3212 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FLOATINGPOINTTOK_in_ctfTypeSpecifier3246 = new BitSet(
            new long[] { 0x0000200000000000L });
    public static final BitSet FOLLOW_ctfBody_in_ctfTypeSpecifier3248 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_INTEGERTOK_in_ctfTypeSpecifier3263 = new BitSet(
            new long[] { 0x0000200000000000L });
    public static final BitSet FOLLOW_ctfBody_in_ctfTypeSpecifier3265 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRINGTOK_in_ctfTypeSpecifier3280 = new BitSet(
            new long[] { 0x0000200000000002L });
    public static final BitSet FOLLOW_ctfBody_in_ctfTypeSpecifier3282 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LCURL_in_ctfBody3320 = new BitSet(
            new long[] { 0x094240007FFFFFF0L, 0x0000000000004090L });
    public static final BitSet FOLLOW_ctfAssignmentExpressionList_in_ctfBody3322 = new BitSet(
            new long[] { 0x0000400000000000L });
    public static final BitSet FOLLOW_RCURL_in_ctfBody3325 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ctfAssignmentExpression_in_ctfAssignmentExpressionList3344 = new BitSet(
            new long[] { 0x0000800000000000L });
    public static final BitSet FOLLOW_TERM_in_ctfAssignmentExpressionList3346 = new BitSet(
            new long[] { 0x094200007FFFFFF2L, 0x0000000000004090L });
    public static final BitSet FOLLOW_unaryExpression_in_ctfAssignmentExpression3381 = new BitSet(
            new long[] { 0x0000006000000000L });
    public static final BitSet FOLLOW_ASSIGNMENT_in_ctfAssignmentExpression3400 = new BitSet(
            new long[] { 0x0942000060170210L, 0x0000000000004090L });
    public static final BitSet FOLLOW_unaryExpression_in_ctfAssignmentExpression3404 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_TYPE_ASSIGNMENT_in_ctfAssignmentExpression3438 = new BitSet(
            new long[] { 0x000000001FCDFDE0L, 0x0000000000004000L });
    public static final BitSet FOLLOW_typeSpecifier_in_ctfAssignmentExpression3443 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_declarationSpecifiers_in_ctfAssignmentExpression3493 = new BitSet(
            new long[] { 0x0001000000000000L, 0x0000000000004000L });
    public static final BitSet FOLLOW_declaratorList_in_ctfAssignmentExpression3497 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_typealiasDecl_in_ctfAssignmentExpression3525 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred1_CTFParser470 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ctfKeyword_in_synpred2_CTFParser492 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_synpred3_CTFParser512 = new BitSet(
            new long[] { 0x0000000000000002L });

}
