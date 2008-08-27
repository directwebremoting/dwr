/* Generated By:JavaCC: Do not edit this line. JavaccJsonParserTokenManager.java */
/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.json.parse.javacc;

@SuppressWarnings("unused")
public class JavaccJsonParserTokenManager implements JavaccJsonParserConstants
{
    public java.io.PrintStream debugStream = System.out;

    public void setDebugStream(java.io.PrintStream ds)
    {
        debugStream = ds;
    }

    private final int jjMoveStringLiteralDfa0_3()
    {
        return jjMoveNfa_3(0, 0);
    }

    private final void jjCheckNAdd(int state)
    {
        if (jjrounds[state] != jjround)
        {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private final void jjAddStates(int start, int end)
    {
        do
        {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        }
        while (start++ != end);
    }

    private final void jjCheckNAddTwoStates(int state1, int state2)
    {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end)
    {
        do
        {
            jjCheckNAdd(jjnextStates[start]);
        }
        while (start++ != end);
    }

    private final void jjCheckNAddStates(int start)
    {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    private final int jjMoveNfa_3(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 4;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (;;)
        {
            if (++jjround == 0x7fffffff)
            {
                ReInitRounds();
            }
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0x3ff000000000000L & l) != 0L)
                        {
                            jjstateSet[jjnewStateCnt++] = 1;
                        }
                        break;
                    case 1:
                        if ((0x3ff000000000000L & l) != 0L)
                        {
                            jjstateSet[jjnewStateCnt++] = 2;
                        }
                        break;
                    case 2:
                        if ((0x3ff000000000000L & l) != 0L)
                        {
                            jjstateSet[jjnewStateCnt++] = 3;
                        }
                        break;
                    case 3:
                        if ((0x3ff000000000000L & l) != 0L && kind > 11)
                        {
                            kind = 11;
                        }
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0x7e0000007eL & l) != 0L)
                        {
                            jjstateSet[jjnewStateCnt++] = 1;
                        }
                        break;
                    case 1:
                        if ((0x7e0000007eL & l) != 0L)
                        {
                            jjstateSet[jjnewStateCnt++] = 2;
                        }
                        break;
                    case 2:
                        if ((0x7e0000007eL & l) != 0L)
                        {
                            jjstateSet[jjnewStateCnt++] = 3;
                        }
                        break;
                    case 3:
                        if ((0x7e0000007eL & l) != 0L && kind > 11)
                        {
                            kind = 11;
                        }
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int hiByte = (curChar >> 8);
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 077);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 4 - (jjnewStateCnt = startsAt)))
            {
                return curPos;
            }
            try
            {
                curChar = input_stream.readChar();
            }
            catch (java.io.IOException e)
            {
                return curPos;
            }
        }
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0)
    {
        switch (pos)
        {
        default:
            return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0)
    {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try
        {
            curChar = input_stream.readChar();
        }
        catch (java.io.IOException e)
        {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_0()
    {
        switch (curChar)
        {
        case 34:
            return jjStopAtPos(0, 4);
        case 44:
            return jjStopAtPos(0, 13);
        case 45:
            return jjStopAtPos(0, 21);
        case 46:
            return jjStopAtPos(0, 22);
        case 58:
            return jjStopAtPos(0, 15);
        case 91:
            return jjStopAtPos(0, 16);
        case 93:
            return jjStopAtPos(0, 17);
        case 102:
            return jjMoveStringLiteralDfa1_0(0x80000L);
        case 110:
            return jjMoveStringLiteralDfa1_0(0x100000L);
        case 116:
            return jjMoveStringLiteralDfa1_0(0x40000L);
        case 123:
            return jjStopAtPos(0, 12);
        case 125:
            return jjStopAtPos(0, 14);
        default:
            return jjMoveNfa_0(0, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0)
    {
        try
        {
            curChar = input_stream.readChar();
        }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch (curChar)
        {
        case 97:
            return jjMoveStringLiteralDfa2_0(active0, 0x80000L);
        case 114:
            return jjMoveStringLiteralDfa2_0(active0, 0x40000L);
        case 117:
            return jjMoveStringLiteralDfa2_0(active0, 0x100000L);
        default:
            break;
        }
        return jjStartNfa_0(0, active0);
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
        {
            return jjStartNfa_0(0, old0);
        }
        try
        {
            curChar = input_stream.readChar();
        }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(1, active0);
            return 2;
        }
        switch (curChar)
        {
        case 108:
            return jjMoveStringLiteralDfa3_0(active0, 0x180000L);
        case 117:
            return jjMoveStringLiteralDfa3_0(active0, 0x40000L);
        default:
            break;
        }
        return jjStartNfa_0(1, active0);
    }

    private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
        {
            return jjStartNfa_0(1, old0);
        }
        try
        {
            curChar = input_stream.readChar();
        }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(2, active0);
            return 3;
        }
        switch (curChar)
        {
        case 101:
            if ((active0 & 0x40000L) != 0L)
            {
                return jjStopAtPos(3, 18);
            }
            break;
        case 108:
            if ((active0 & 0x100000L) != 0L)
            {
                return jjStopAtPos(3, 20);
            }
            break;
        case 115:
            return jjMoveStringLiteralDfa4_0(active0, 0x80000L);
        default:
            break;
        }
        return jjStartNfa_0(2, active0);
    }

    private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
    {
        if (((active0 &= old0)) == 0L)
        {
            return jjStartNfa_0(2, old0);
        }
        try
        {
            curChar = input_stream.readChar();
        }
        catch (java.io.IOException e)
        {
            jjStopStringLiteralDfa_0(3, active0);
            return 4;
        }
        switch (curChar)
        {
        case 101:
            if ((active0 & 0x80000L) != 0L)
            {
                return jjStopAtPos(4, 19);
            }
            break;
        default:
            break;
        }
        return jjStartNfa_0(3, active0);
    }

    private final int jjMoveNfa_0(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 6;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (;;)
        {
            if (++jjround == 0x7fffffff)
            {
                ReInitRounds();
            }
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0x3ff000000000000L & l) != 0L)
                        {
                            if (kind > 3)
                            {
                                kind = 3;
                            }
                        }
                        else if ((0x100000600L & l) != 0L)
                        {
                            if (kind > 1)
                            {
                                kind = 1;
                            }
                        }
                        if ((0x3fe000000000000L & l) != 0L)
                        {
                            if (kind > 3)
                            {
                                kind = 3;
                            }
                            jjCheckNAdd(5);
                        }
                        break;
                    case 2:
                        if ((0x280000000000L & l) != 0L)
                        {
                            kind = 2;
                        }
                        break;
                    case 3:
                        if ((0x3ff000000000000L & l) != 0L && kind > 3)
                        {
                            kind = 3;
                        }
                        break;
                    case 4:
                        if ((0x3fe000000000000L & l) == 0L)
                        {
                            break;
                        }
                        if (kind > 3)
                        {
                            kind = 3;
                        }
                        jjCheckNAdd(5);
                        break;
                    case 5:
                        if ((0x3ff000000000000L & l) == 0L)
                        {
                            break;
                        }
                        if (kind > 3)
                        {
                            kind = 3;
                        }
                        jjCheckNAdd(5);
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0x2000000020L & l) == 0L)
                        {
                            break;
                        }
                        kind = 2;
                        jjstateSet[jjnewStateCnt++] = 2;
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int hiByte = (curChar >> 8);
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 077);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 6 - (jjnewStateCnt = startsAt)))
            {
                return curPos;
            }
            try
            {
                curChar = input_stream.readChar();
            }
            catch (java.io.IOException e)
            {
                return curPos;
            }
        }
    }

    private final int jjStopStringLiteralDfa_2(int pos, long active0)
    {
        switch (pos)
        {
        default:
            return -1;
        }
    }

    private final int jjStartNfa_2(int pos, long active0)
    {
        return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
    }

    private final int jjStartNfaWithStates_2(int pos, int kind, int state)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try
        {
            curChar = input_stream.readChar();
        }
        catch (java.io.IOException e)
        {
            return pos + 1;
        }
        return jjMoveNfa_2(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_2()
    {
        switch (curChar)
        {
        case 117:
            return jjStopAtPos(0, 9);
        default:
            return jjMoveNfa_2(0, 0);
        }
    }

    private final int jjMoveNfa_2(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 1;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (;;)
        {
            if (++jjround == 0x7fffffff)
            {
                ReInitRounds();
            }
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0x800400000000L & l) != 0L)
                        {
                            kind = 8;
                        }
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0x14404410000000L & l) != 0L)
                        {
                            kind = 8;
                        }
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int hiByte = (curChar >> 8);
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 077);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
            {
                return curPos;
            }
            try
            {
                curChar = input_stream.readChar();
            }
            catch (java.io.IOException e)
            {
                return curPos;
            }
        }
    }

    private final int jjStopStringLiteralDfa_1(int pos, long active0)
    {
        switch (pos)
        {
        default:
            return -1;
        }
    }

    private final int jjStartNfa_1(int pos, long active0)
    {
        return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
    }

    private final int jjStartNfaWithStates_1(int pos, int kind, int state)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try
        {
            curChar = input_stream.readChar();
        }
        catch (java.io.IOException e)
        {
            return pos + 1;
        }
        return jjMoveNfa_1(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_1()
    {
        switch (curChar)
        {
        case 92:
            return jjStopAtPos(0, 5);
        default:
            return jjMoveNfa_1(0, 0);
        }
    }

    static final long[] jjbitVec0 = { 0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL };

    static final long[] jjbitVec2 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private final int jjMoveNfa_1(int startState, int curPos)
    {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 2;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (;;)
        {
            if (++jjround == 0x7fffffff)
            {
                ReInitRounds();
            }
            if (curChar < 64)
            {
                long l = 1L << curChar;
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0xfffffffbffffffffL & l) != 0L)
                        {
                            if (kind > 7)
                            {
                                kind = 7;
                            }
                        }
                        else if (curChar == 34)
                        {
                            if (kind > 6)
                            {
                                kind = 6;
                            }
                        }
                        break;
                    case 1:
                        if ((0xfffffffbffffffffL & l) != 0L)
                        {
                            kind = 7;
                        }
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else if (curChar < 128)
            {
                long l = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if ((0xffffffffefffffffL & l) != 0L)
                        {
                            kind = 7;
                        }
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            else
            {
                int hiByte = (curChar >> 8);
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 077);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do
                {
                    switch (jjstateSet[--i])
                    {
                    case 0:
                        if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 7)
                        {
                            kind = 7;
                        }
                        break;
                    default:
                        break;
                    }
                }
                while (i != startsAt);
            }
            if (kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 2 - (jjnewStateCnt = startsAt)))
            {
                return curPos;
            }
            try
            {
                curChar = input_stream.readChar();
            }
            catch (java.io.IOException e)
            {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = {};

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
    {
        switch (hiByte)
        {
        case 0:
            return ((jjbitVec2[i2] & l2) != 0L);
        default:
            if ((jjbitVec0[i1] & l1) != 0L)
            {
                return true;
            }
            return false;
        }
    }

    public static final String[] jjstrLiteralImages = { "", null, null, null, "\42", null, null, null, null, null, null, null, "\173", "\54", "\175", "\72", "\133", "\135", "\164\162\165\145", "\146\141\154\163\145", "\156\165\154\154", "\55", "\56", };

    public static final String[] lexStateNames = { "DEFAULT", "STRING_STATE", "ESC_STATE", "HEX_STATE", };

    public static final int[] jjnewLexState = { -1, -1, -1, -1, 1, 2, 0, -1, 1, 3, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, };

    static final long[] jjtoToken = { 0x7ff9ddL, };

    static final long[] jjtoSkip = { 0x2L, };

    static final long[] jjtoMore = { 0x220L, };

    protected SimpleCharStream input_stream;

    private final int[] jjrounds = new int[6];

    private final int[] jjstateSet = new int[12];

    protected char curChar;

    public JavaccJsonParserTokenManager(SimpleCharStream stream)
    {
        if (SimpleCharStream.staticFlag)
        {
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        }
        input_stream = stream;
    }

    public JavaccJsonParserTokenManager(SimpleCharStream stream, int lexState)
    {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream)
    {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds()
    {
        int i;
        jjround = 0x80000001;
        for (i = 6; i-- > 0;)
        {
            jjrounds[i] = 0x80000000;
        }
    }

    public void ReInit(SimpleCharStream stream, int lexState)
    {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState)
    {
        if (lexState >= 4 || lexState < 0)
        {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
        }
        else
        {
            curLexState = lexState;
        }
    }

    protected Token jjFillToken()
    {
        Token t = Token.newToken(jjmatchedKind);
        t.kind = jjmatchedKind;
        String im = jjstrLiteralImages[jjmatchedKind];
        t.image = (im == null) ? input_stream.GetImage() : im;
        t.beginLine = input_stream.getBeginLine();
        t.beginColumn = input_stream.getBeginColumn();
        t.endLine = input_stream.getEndLine();
        t.endColumn = input_stream.getEndColumn();
        return t;
    }

    int curLexState = 0;

    int defaultLexState = 0;

    int jjnewStateCnt;

    int jjround;

    int jjmatchedPos;

    int jjmatchedKind;

    public Token getNextToken()
    {
        int kind;
        Token specialToken = null;
        Token matchedToken;
        int curPos = 0;

        EOFLoop: for (;;)
        {
            try
            {
                curChar = input_stream.BeginToken();
            }
            catch (java.io.IOException e)
            {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                return matchedToken;
            }

            for (;;)
            {
                switch (curLexState)
                {
                case 0:
                    jjmatchedKind = 0x7fffffff;
                    jjmatchedPos = 0;
                    curPos = jjMoveStringLiteralDfa0_0();
                    break;
                case 1:
                    jjmatchedKind = 0x7fffffff;
                    jjmatchedPos = 0;
                    curPos = jjMoveStringLiteralDfa0_1();
                    break;
                case 2:
                    jjmatchedKind = 0x7fffffff;
                    jjmatchedPos = 0;
                    curPos = jjMoveStringLiteralDfa0_2();
                    break;
                case 3:
                    jjmatchedKind = 0x7fffffff;
                    jjmatchedPos = 0;
                    curPos = jjMoveStringLiteralDfa0_3();
                    break;
                }
                if (jjmatchedKind != 0x7fffffff)
                {
                    if (jjmatchedPos + 1 < curPos)
                    {
                        input_stream.backup(curPos - jjmatchedPos - 1);
                    }
                    if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
                    {
                        matchedToken = jjFillToken();
                        if (jjnewLexState[jjmatchedKind] != -1)
                        {
                            curLexState = jjnewLexState[jjmatchedKind];
                        }
                        return matchedToken;
                    }
                    else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
                    {
                        if (jjnewLexState[jjmatchedKind] != -1)
                        {
                            curLexState = jjnewLexState[jjmatchedKind];
                        }
                        continue EOFLoop;
                    }
                    if (jjnewLexState[jjmatchedKind] != -1)
                    {
                        curLexState = jjnewLexState[jjmatchedKind];
                    }
                    curPos = 0;
                    jjmatchedKind = 0x7fffffff;
                    try
                    {
                        curChar = input_stream.readChar();
                        continue;
                    }
                    catch (java.io.IOException e1)
                    {
                    }
                }
                int error_line = input_stream.getEndLine();
                int error_column = input_stream.getEndColumn();
                String error_after = null;
                boolean EOFSeen = false;
                try
                {
                    input_stream.readChar();
                    input_stream.backup(1);
                }
                catch (java.io.IOException e1)
                {
                    EOFSeen = true;
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                    if (curChar == '\n' || curChar == '\r')
                    {
                        error_line++;
                        error_column = 0;
                    }
                    else
                    {
                        error_column++;
                    }
                }
                if (!EOFSeen)
                {
                    input_stream.backup(1);
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                }
                throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
            }
        }
    }

}
