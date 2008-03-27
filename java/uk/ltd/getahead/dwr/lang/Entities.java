/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package uk.ltd.getahead.dwr.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * <p>Provides HTML and XML entity utilities.</p>
 *
 * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
 * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
 * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
 * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
 * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
 *
 * @author <a href="mailto:alex@purpletech.com">Alexander Day Chaffee</a>
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @since 2.0
 * @version $Id: Entities.java,v 1.13 2003/08/18 02:22:22 bayard Exp $
 */
/**
 * @author
 */
/**
 * @author
 */
/**
 * @author
 */
class Entities {

    private static final String[][] BASIC_ARRAY = {
        {"quot", "34"}, // " - double-quote //$NON-NLS-1$ //$NON-NLS-2$
        {"amp", "38"}, // & - ampersand //$NON-NLS-1$ //$NON-NLS-2$
        {"lt", "60"}, // < - less-than //$NON-NLS-1$ //$NON-NLS-2$
        {"gt", "62"}, // > - greater-than //$NON-NLS-1$ //$NON-NLS-2$
    };

    private static final String[][] APOS_ARRAY = {
        {"apos", "39"}, // XML apostrophe //$NON-NLS-1$ //$NON-NLS-2$
    };

    // package scoped for testing
    static final String[][] ISO8859_1_ARRAY = {
        {"nbsp", "160"}, // non-breaking space //$NON-NLS-1$ //$NON-NLS-2$
        {"iexcl", "161"}, //inverted exclamation mark //$NON-NLS-1$ //$NON-NLS-2$
        {"cent", "162"}, //cent sign //$NON-NLS-1$ //$NON-NLS-2$
        {"pound", "163"}, //pound sign //$NON-NLS-1$ //$NON-NLS-2$
        {"curren", "164"}, //currency sign //$NON-NLS-1$ //$NON-NLS-2$
        {"yen", "165"}, //yen sign = yuan sign //$NON-NLS-1$ //$NON-NLS-2$
        {"brvbar", "166"}, //broken bar = broken vertical bar //$NON-NLS-1$ //$NON-NLS-2$
        {"sect", "167"}, //section sign //$NON-NLS-1$ //$NON-NLS-2$
        {"uml", "168"}, //diaeresis = spacing diaeresis //$NON-NLS-1$ //$NON-NLS-2$
        {"copy", "169"}, // © - copyright sign //$NON-NLS-1$ //$NON-NLS-2$
        {"ordf", "170"}, //feminine ordinal indicator //$NON-NLS-1$ //$NON-NLS-2$
        {"laquo", "171"}, //left-pointing double angle quotation mark = left pointing guillemet //$NON-NLS-1$ //$NON-NLS-2$
        {"not", "172"}, //not sign //$NON-NLS-1$ //$NON-NLS-2$
        {"shy", "173"}, //soft hyphen = discretionary hyphen //$NON-NLS-1$ //$NON-NLS-2$
        {"reg", "174"}, // ½ - registered trademark sign //$NON-NLS-1$ //$NON-NLS-2$
        {"macr", "175"}, //macron = spacing macron = overline = APL overbar //$NON-NLS-1$ //$NON-NLS-2$
        {"deg", "176"}, //degree sign //$NON-NLS-1$ //$NON-NLS-2$
        {"plusmn", "177"}, //plus-minus sign = plus-or-minus sign //$NON-NLS-1$ //$NON-NLS-2$
        {"sup2", "178"}, //superscript two = superscript digit two = squared //$NON-NLS-1$ //$NON-NLS-2$
        {"sup3", "179"}, //superscript three = superscript digit three = cubed //$NON-NLS-1$ //$NON-NLS-2$
        {"acute", "180"}, //acute accent = spacing acute //$NON-NLS-1$ //$NON-NLS-2$
        {"micro", "181"}, //micro sign //$NON-NLS-1$ //$NON-NLS-2$
        {"para", "182"}, //pilcrow sign = paragraph sign //$NON-NLS-1$ //$NON-NLS-2$
        {"middot", "183"}, //middle dot = Georgian comma = Greek middle dot //$NON-NLS-1$ //$NON-NLS-2$
        {"cedil", "184"}, //cedilla = spacing cedilla //$NON-NLS-1$ //$NON-NLS-2$
        {"sup1", "185"}, //superscript one = superscript digit one //$NON-NLS-1$ //$NON-NLS-2$
        {"ordm", "186"}, //masculine ordinal indicator //$NON-NLS-1$ //$NON-NLS-2$
        {"raquo", "187"}, //right-pointing double angle quotation mark = right pointing guillemet //$NON-NLS-1$ //$NON-NLS-2$
        {"frac14", "188"}, //vulgar fraction one quarter = fraction one quarter //$NON-NLS-1$ //$NON-NLS-2$
        {"frac12", "189"}, //vulgar fraction one half = fraction one half //$NON-NLS-1$ //$NON-NLS-2$
        {"frac34", "190"}, //vulgar fraction three quarters = fraction three quarters //$NON-NLS-1$ //$NON-NLS-2$
        {"iquest", "191"}, //inverted question mark = turned question mark //$NON-NLS-1$ //$NON-NLS-2$
        {"Agrave", "192"}, // À - uppercase A, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Aacute", "193"}, // Á - uppercase A, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Acirc", "194"}, // Â - uppercase A, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Atilde", "195"}, // Ã - uppercase A, tilde //$NON-NLS-1$ //$NON-NLS-2$
        {"Auml", "196"}, // Ž - uppercase A, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"Aring", "197"}, // ? - uppercase A, ring //$NON-NLS-1$ //$NON-NLS-2$
        {"AElig", "198"}, // ’ - uppercase AE //$NON-NLS-1$ //$NON-NLS-2$
        {"Ccedil", "199"}, // € - uppercase C, cedilla //$NON-NLS-1$ //$NON-NLS-2$
        {"Egrave", "200"}, // È - uppercase E, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Eacute", "201"}, // ? - uppercase E, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Ecirc", "202"}, // Ê - uppercase E, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Euml", "203"}, // Ë - uppercase E, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"Igrave", "204"}, // Ì - uppercase I, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Iacute", "205"}, // Í - uppercase I, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Icirc", "206"}, // Î - uppercase I, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Iuml", "207"}, // Ï - uppercase I, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"ETH", "208"}, // Ð - uppercase Eth, Icelandic //$NON-NLS-1$ //$NON-NLS-2$
        {"Ntilde", "209"}, // ¥ - uppercase N, tilde //$NON-NLS-1$ //$NON-NLS-2$
        {"Ograve", "210"}, // Ò - uppercase O, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Oacute", "211"}, // Ó - uppercase O, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Ocirc", "212"}, // Ô - uppercase O, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Otilde", "213"}, // Õ - uppercase O, tilde //$NON-NLS-1$ //$NON-NLS-2$
        {"Ouml", "214"}, // ™ - uppercase O, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"times", "215"}, //multiplication sign //$NON-NLS-1$ //$NON-NLS-2$
        {"Oslash", "216"}, // Ø - uppercase O, slash //$NON-NLS-1$ //$NON-NLS-2$
        {"Ugrave", "217"}, // Ù - uppercase U, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Uacute", "218"}, // Ú - uppercase U, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Ucirc", "219"}, // Û - uppercase U, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"Uuml", "220"}, // š - uppercase U, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"Yacute", "221"}, // Ý - uppercase Y, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"THORN", "222"}, // Þ - uppercase THORN, Icelandic //$NON-NLS-1$ //$NON-NLS-2$
        {"szlig", "223"}, // á - lowercase sharps, German //$NON-NLS-1$ //$NON-NLS-2$
        {"agrave", "224"}, // … - lowercase a, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"aacute", "225"}, //   - lowercase a, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"acirc", "226"}, // ƒ - lowercase a, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"atilde", "227"}, // ã - lowercase a, tilde //$NON-NLS-1$ //$NON-NLS-2$
        {"auml", "228"}, // „ - lowercase a, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"aring", "229"}, // † - lowercase a, ring //$NON-NLS-1$ //$NON-NLS-2$
        {"aelig", "230"}, // ‘ - lowercase ae //$NON-NLS-1$ //$NON-NLS-2$
        {"ccedil", "231"}, // ‡ - lowercase c, cedilla //$NON-NLS-1$ //$NON-NLS-2$
        {"egrave", "232"}, // Š - lowercase e, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"eacute", "233"}, // ‚ - lowercase e, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"ecirc", "234"}, // ˆ - lowercase e, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"euml", "235"}, // ‰ - lowercase e, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"igrave", "236"}, // ? - lowercase i, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"iacute", "237"}, // ¡ - lowercase i, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"icirc", "238"}, // Œ - lowercase i, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"iuml", "239"}, // ‹ - lowercase i, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"eth", "240"}, // ð - lowercase eth, Icelandic //$NON-NLS-1$ //$NON-NLS-2$
        {"ntilde", "241"}, // ¤ - lowercase n, tilde //$NON-NLS-1$ //$NON-NLS-2$
        {"ograve", "242"}, // • - lowercase o, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"oacute", "243"}, // ¢ - lowercase o, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"ocirc", "244"}, // “ - lowercase o, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"otilde", "245"}, // õ - lowercase o, tilde //$NON-NLS-1$ //$NON-NLS-2$
        {"ouml", "246"}, // ” - lowercase o, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"divide", "247"}, // division sign //$NON-NLS-1$ //$NON-NLS-2$
        {"oslash", "248"}, // ° - lowercase o, slash //$NON-NLS-1$ //$NON-NLS-2$
        {"ugrave", "249"}, // — - lowercase u, grave accent //$NON-NLS-1$ //$NON-NLS-2$
        {"uacute", "250"}, // £ - lowercase u, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"ucirc", "251"}, // – - lowercase u, circumflex accent //$NON-NLS-1$ //$NON-NLS-2$
        {"uuml", "252"}, // ? - lowercase u, umlaut //$NON-NLS-1$ //$NON-NLS-2$
        {"yacute", "253"}, // ² - lowercase y, acute accent //$NON-NLS-1$ //$NON-NLS-2$
        {"thorn", "254"}, // þ - lowercase thorn, Icelandic //$NON-NLS-1$ //$NON-NLS-2$
        {"yuml", "255"}, // ˜ - lowercase y, umlaut //$NON-NLS-1$ //$NON-NLS-2$
    };

    // http://www.w3.org/TR/REC-html40/sgml/entities.html
    // package scoped for testing
    static final String[][] HTML40_ARRAY = {
// <!-- Latin Extended-B -->
        {"fnof", "402"}, //latin small f with hook = function= florin, U+0192 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- Greek -->
        {"Alpha", "913"}, //greek capital letter alpha, U+0391 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Beta", "914"}, //greek capital letter beta, U+0392 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Gamma", "915"}, //greek capital letter gamma,U+0393 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Delta", "916"}, //greek capital letter delta,U+0394 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Epsilon", "917"}, //greek capital letter epsilon, U+0395 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Zeta", "918"}, //greek capital letter zeta, U+0396 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Eta", "919"}, //greek capital letter eta, U+0397 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Theta", "920"}, //greek capital letter theta,U+0398 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Iota", "921"}, //greek capital letter iota, U+0399 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Kappa", "922"}, //greek capital letter kappa, U+039A --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Lambda", "923"}, //greek capital letter lambda,U+039B ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Mu", "924"}, //greek capital letter mu, U+039C --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Nu", "925"}, //greek capital letter nu, U+039D --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Xi", "926"}, //greek capital letter xi, U+039E ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Omicron", "927"}, //greek capital letter omicron, U+039F --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Pi", "928"}, //greek capital letter pi, U+03A0 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Rho", "929"}, //greek capital letter rho, U+03A1 --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- there is no Sigmaf, and no U+03A2 character either --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Sigma", "931"}, //greek capital letter sigma,U+03A3 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Tau", "932"}, //greek capital letter tau, U+03A4 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Upsilon", "933"}, //greek capital letter upsilon,U+03A5 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Phi", "934"}, //greek capital letter phi,U+03A6 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Chi", "935"}, //greek capital letter chi, U+03A7 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Psi", "936"}, //greek capital letter psi,U+03A8 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Omega", "937"}, //greek capital letter omega,U+03A9 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"alpha", "945"}, //greek small letter alpha,U+03B1 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"beta", "946"}, //greek small letter beta, U+03B2 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"gamma", "947"}, //greek small letter gamma,U+03B3 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"delta", "948"}, //greek small letter delta,U+03B4 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"epsilon", "949"}, //greek small letter epsilon,U+03B5 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"zeta", "950"}, //greek small letter zeta, U+03B6 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"eta", "951"}, //greek small letter eta, U+03B7 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"theta", "952"}, //greek small letter theta,U+03B8 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"iota", "953"}, //greek small letter iota, U+03B9 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"kappa", "954"}, //greek small letter kappa,U+03BA ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lambda", "955"}, //greek small letter lambda,U+03BB ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"mu", "956"}, //greek small letter mu, U+03BC ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"nu", "957"}, //greek small letter nu, U+03BD ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"xi", "958"}, //greek small letter xi, U+03BE ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"omicron", "959"}, //greek small letter omicron, U+03BF NEW --> //$NON-NLS-1$ //$NON-NLS-2$
        {"pi", "960"}, //greek small letter pi, U+03C0 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"rho", "961"}, //greek small letter rho, U+03C1 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"sigmaf", "962"}, //greek small letter final sigma,U+03C2 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"sigma", "963"}, //greek small letter sigma,U+03C3 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"tau", "964"}, //greek small letter tau, U+03C4 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"upsilon", "965"}, //greek small letter upsilon,U+03C5 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"phi", "966"}, //greek small letter phi, U+03C6 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"chi", "967"}, //greek small letter chi, U+03C7 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"psi", "968"}, //greek small letter psi, U+03C8 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"omega", "969"}, //greek small letter omega,U+03C9 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"thetasym", "977"}, //greek small letter theta symbol,U+03D1 NEW --> //$NON-NLS-1$ //$NON-NLS-2$
        {"upsih", "978"}, //greek upsilon with hook symbol,U+03D2 NEW --> //$NON-NLS-1$ //$NON-NLS-2$
        {"piv", "982"}, //greek pi symbol, U+03D6 ISOgrk3 --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- General Punctuation -->
        {"bull", "8226"}, //bullet = black small circle,U+2022 ISOpub  --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- bullet is NOT the same as bullet operator, U+2219 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"hellip", "8230"}, //horizontal ellipsis = three dot leader,U+2026 ISOpub  --> //$NON-NLS-1$ //$NON-NLS-2$
        {"prime", "8242"}, //prime = minutes = feet, U+2032 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Prime", "8243"}, //double prime = seconds = inches,U+2033 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"oline", "8254"}, //overline = spacing overscore,U+203E NEW --> //$NON-NLS-1$ //$NON-NLS-2$
        {"frasl", "8260"}, //fraction slash, U+2044 NEW --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- Letterlike Symbols -->
        {"weierp", "8472"}, //script capital P = power set= Weierstrass p, U+2118 ISOamso --> //$NON-NLS-1$ //$NON-NLS-2$
        {"image", "8465"}, //blackletter capital I = imaginary part,U+2111 ISOamso --> //$NON-NLS-1$ //$NON-NLS-2$
        {"real", "8476"}, //blackletter capital R = real part symbol,U+211C ISOamso --> //$NON-NLS-1$ //$NON-NLS-2$
        {"trade", "8482"}, //trade mark sign, U+2122 ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"alefsym", "8501"}, //alef symbol = first transfinite cardinal,U+2135 NEW --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- alef symbol is NOT the same as hebrew letter alef,U+05D0 although the same glyph could be used to depict both characters -->
// <!-- Arrows -->
        {"larr", "8592"}, //leftwards arrow, U+2190 ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"uarr", "8593"}, //upwards arrow, U+2191 ISOnum--> //$NON-NLS-1$ //$NON-NLS-2$
        {"rarr", "8594"}, //rightwards arrow, U+2192 ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"darr", "8595"}, //downwards arrow, U+2193 ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"harr", "8596"}, //left right arrow, U+2194 ISOamsa --> //$NON-NLS-1$ //$NON-NLS-2$
        {"crarr", "8629"}, //downwards arrow with corner leftwards= carriage return, U+21B5 NEW --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lArr", "8656"}, //leftwards double arrow, U+21D0 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- ISO 10646 does not say that lArr is the same as the 'is implied by' arrowbut also does not have any other character for that function. So ? lArr canbe used for 'is implied by' as ISOtech suggests -->
        {"uArr", "8657"}, //upwards double arrow, U+21D1 ISOamsa --> //$NON-NLS-1$ //$NON-NLS-2$
        {"rArr", "8658"}, //rightwards double arrow,U+21D2 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- ISO 10646 does not say this is the 'implies' character but does not have another character with this function so ?rArr can be used for 'implies' as ISOtech suggests -->
        {"dArr", "8659"}, //downwards double arrow, U+21D3 ISOamsa --> //$NON-NLS-1$ //$NON-NLS-2$
        {"hArr", "8660"}, //left right double arrow,U+21D4 ISOamsa --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- Mathematical Operators -->
        {"forall", "8704"}, //for all, U+2200 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"part", "8706"}, //partial differential, U+2202 ISOtech  --> //$NON-NLS-1$ //$NON-NLS-2$
        {"exist", "8707"}, //there exists, U+2203 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"empty", "8709"}, //empty set = null set = diameter,U+2205 ISOamso --> //$NON-NLS-1$ //$NON-NLS-2$
        {"nabla", "8711"}, //nabla = backward difference,U+2207 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"isin", "8712"}, //element of, U+2208 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"notin", "8713"}, //not an element of, U+2209 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"ni", "8715"}, //contains as member, U+220B ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- should there be a more memorable name than 'ni'? -->
        {"prod", "8719"}, //n-ary product = product sign,U+220F ISOamsb --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- prod is NOT the same character as U+03A0 'greek capital letter pi' thoughthe same glyph might be used for both -->
        {"sum", "8721"}, //n-ary sumation, U+2211 ISOamsb --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- sum is NOT the same character as U+03A3 'greek capital letter sigma'though the same glyph might be used for both -->
        {"minus", "8722"}, //minus sign, U+2212 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lowast", "8727"}, //asterisk operator, U+2217 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"radic", "8730"}, //square root = radical sign,U+221A ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"prop", "8733"}, //proportional to, U+221D ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"infin", "8734"}, //infinity, U+221E ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"ang", "8736"}, //angle, U+2220 ISOamso --> //$NON-NLS-1$ //$NON-NLS-2$
        {"and", "8743"}, //logical and = wedge, U+2227 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"or", "8744"}, //logical or = vee, U+2228 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"cap", "8745"}, //intersection = cap, U+2229 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"cup", "8746"}, //union = cup, U+222A ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"int", "8747"}, //integral, U+222B ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"there4", "8756"}, //therefore, U+2234 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"sim", "8764"}, //tilde operator = varies with = similar to,U+223C ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- tilde operator is NOT the same character as the tilde, U+007E,although the same glyph might be used to represent both  -->
        {"cong", "8773"}, //approximately equal to, U+2245 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"asymp", "8776"}, //almost equal to = asymptotic to,U+2248 ISOamsr --> //$NON-NLS-1$ //$NON-NLS-2$
        {"ne", "8800"}, //not equal to, U+2260 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"equiv", "8801"}, //identical to, U+2261 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"le", "8804"}, //less-than or equal to, U+2264 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"ge", "8805"}, //greater-than or equal to,U+2265 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"sub", "8834"}, //subset of, U+2282 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"sup", "8835"}, //superset of, U+2283 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- note that nsup, 'not a superset of, U+2283' is not covered by the Symbol font encoding and is not included. Should it be, for symmetry?It is in ISOamsn  --> <!ENTITY nsub", "8836"},  //not a subset of, U+2284 ISOamsn -->
        {"sube", "8838"}, //subset of or equal to, U+2286 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"supe", "8839"}, //superset of or equal to,U+2287 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"oplus", "8853"}, //circled plus = direct sum,U+2295 ISOamsb --> //$NON-NLS-1$ //$NON-NLS-2$
        {"otimes", "8855"}, //circled times = vector product,U+2297 ISOamsb --> //$NON-NLS-1$ //$NON-NLS-2$
        {"perp", "8869"}, //up tack = orthogonal to = perpendicular,U+22A5 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"sdot", "8901"}, //dot operator, U+22C5 ISOamsb --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- dot operator is NOT the same character as U+00B7 middle dot -->
// <!-- Miscellaneous Technical -->
        {"lceil", "8968"}, //left ceiling = apl upstile,U+2308 ISOamsc  --> //$NON-NLS-1$ //$NON-NLS-2$
        {"rceil", "8969"}, //right ceiling, U+2309 ISOamsc  --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lfloor", "8970"}, //left floor = apl downstile,U+230A ISOamsc  --> //$NON-NLS-1$ //$NON-NLS-2$
        {"rfloor", "8971"}, //right floor, U+230B ISOamsc  --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lang", "9001"}, //left-pointing angle bracket = bra,U+2329 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- lang is NOT the same character as U+003C 'less than' or U+2039 'single left-pointing angle quotation mark' -->
        {"rang", "9002"}, //right-pointing angle bracket = ket,U+232A ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- rang is NOT the same character as U+003E 'greater than' or U+203A 'single right-pointing angle quotation mark' -->
// <!-- Geometric Shapes -->
        {"loz", "9674"}, //lozenge, U+25CA ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- Miscellaneous Symbols -->
        {"spades", "9824"}, //black spade suit, U+2660 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- black here seems to mean filled as opposed to hollow -->
        {"clubs", "9827"}, //black club suit = shamrock,U+2663 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"hearts", "9829"}, //black heart suit = valentine,U+2665 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"diams", "9830"}, //black diamond suit, U+2666 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$

// <!-- Latin Extended-A -->
        {"OElig", "338"}, //  -- latin capital ligature OE,U+0152 ISOlat2 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"oelig", "339"}, //  -- latin small ligature oe, U+0153 ISOlat2 --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- ligature is a misnomer, this is a separate character in some languages -->
        {"Scaron", "352"}, //  -- latin capital letter S with caron,U+0160 ISOlat2 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"scaron", "353"}, //  -- latin small letter s with caron,U+0161 ISOlat2 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Yuml", "376"}, //  -- latin capital letter Y with diaeresis,U+0178 ISOlat2 --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- Spacing Modifier Letters -->
        {"circ", "710"}, //  -- modifier letter circumflex accent,U+02C6 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"tilde", "732"}, //small tilde, U+02DC ISOdia --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- General Punctuation -->
        {"ensp", "8194"}, //en space, U+2002 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"emsp", "8195"}, //em space, U+2003 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"thinsp", "8201"}, //thin space, U+2009 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"zwnj", "8204"}, //zero width non-joiner,U+200C NEW RFC 2070 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"zwj", "8205"}, //zero width joiner, U+200D NEW RFC 2070 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lrm", "8206"}, //left-to-right mark, U+200E NEW RFC 2070 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"rlm", "8207"}, //right-to-left mark, U+200F NEW RFC 2070 --> //$NON-NLS-1$ //$NON-NLS-2$
        {"ndash", "8211"}, //en dash, U+2013 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"mdash", "8212"}, //em dash, U+2014 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lsquo", "8216"}, //left single quotation mark,U+2018 ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"rsquo", "8217"}, //right single quotation mark,U+2019 ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"sbquo", "8218"}, //single low-9 quotation mark, U+201A NEW --> //$NON-NLS-1$ //$NON-NLS-2$
        {"ldquo", "8220"}, //left double quotation mark,U+201C ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"rdquo", "8221"}, //right double quotation mark,U+201D ISOnum --> //$NON-NLS-1$ //$NON-NLS-2$
        {"bdquo", "8222"}, //double low-9 quotation mark, U+201E NEW --> //$NON-NLS-1$ //$NON-NLS-2$
        {"dagger", "8224"}, //dagger, U+2020 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"Dagger", "8225"}, //double dagger, U+2021 ISOpub --> //$NON-NLS-1$ //$NON-NLS-2$
        {"permil", "8240"}, //per mille sign, U+2030 ISOtech --> //$NON-NLS-1$ //$NON-NLS-2$
        {"lsaquo", "8249"}, //single left-pointing angle quotation mark,U+2039 ISO proposed --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- lsaquo is proposed but not yet ISO standardized -->
        {"rsaquo", "8250"}, //single right-pointing angle quotation mark,U+203A ISO proposed --> //$NON-NLS-1$ //$NON-NLS-2$
// <!-- rsaquo is proposed but not yet ISO standardized -->
        {"euro", "8364"}, //  -- euro sign, U+20AC NEW --> //$NON-NLS-1$ //$NON-NLS-2$
    };

    /**
     * <p>The set of entities supported by standard XML.</p>
     */
    public static final Entities XML;

    /**
     * <p>The set of entities supported by HTML 3.2.</p>
     */
    public static final Entities HTML32;

    /**
     * <p>The set of entities supported by HTML 4.0.</p>
     */
    public static final Entities HTML40;

    static {
        XML = new Entities();
        XML.addEntities(BASIC_ARRAY);
        XML.addEntities(APOS_ARRAY);
    }

    static {
        HTML32 = new Entities();
        HTML32.addEntities(BASIC_ARRAY);
        HTML32.addEntities(ISO8859_1_ARRAY);
    }

    static {
        HTML40 = new Entities();
        fillWithHtml40Entities(HTML40);
    }

    static void fillWithHtml40Entities(Entities entities) {
        entities.addEntities(BASIC_ARRAY);
        entities.addEntities(ISO8859_1_ARRAY);
        entities.addEntities(HTML40_ARRAY);
    }

    static interface EntityMap {
        /**
         * @param name
         * @param value
         */
        void add(String name, int value);

        /**
         * @param value
         * @return the string
         */
        String name(int value);

        /**
         * @param name
         * @return the number
         */
        int value(String name);
    }

    static class PrimitiveEntityMap implements EntityMap {
        private Map mapNameToValue = new HashMap();
        private IntHashMap mapValueToName = new IntHashMap();

        public void add(String name, int value) {
            mapNameToValue.put(name, new Integer(value));
            mapValueToName.put(value, name);
        }

        public String name(int value) {
            return (String) mapValueToName.get(value);
        }

        public int value(String name) {
            Object value = mapNameToValue.get(name);
            if (value == null)
                return -1;
            return ((Integer) value).intValue();
        }
    }


    static abstract class MapIntMap implements Entities.EntityMap {
        protected Map mapNameToValue;
        protected Map mapValueToName;

        public void add(String name, int value) {
            mapNameToValue.put(name, new Integer(value));
            mapValueToName.put(new Integer(value), name);
        }

        public String name(int value) {
            return (String) mapValueToName.get(new Integer(value));
        }

        public int value(String name) {
            Object value = mapNameToValue.get(name);
            if (value == null)
                return -1;
            return ((Integer) value).intValue();
        }
    }

    static class HashEntityMap extends MapIntMap {
        /**
         * 
         */
        public HashEntityMap() {
            mapNameToValue = new HashMap();
            mapValueToName = new HashMap();
        }
    }

    /**
     * @author
     */
    static class TreeEntityMap extends MapIntMap {
        /**
         * 
         */
        public TreeEntityMap() {
            mapNameToValue = new TreeMap();
            mapValueToName = new TreeMap();
        }
    }

    static class LookupEntityMap extends PrimitiveEntityMap {
        private String[] lookupTable;
        private int LOOKUP_TABLE_SIZE = 256;

        public String name(int value) {
            if (value < LOOKUP_TABLE_SIZE) {
                return lookupTable()[value];
            }
            return super.name(value);
        }

        private String[] lookupTable() {
            if (lookupTable == null) {
                createLookupTable();
            }
            return lookupTable;
        }

        private void createLookupTable() {
            lookupTable = new String[LOOKUP_TABLE_SIZE];
            for (int i = 0; i < LOOKUP_TABLE_SIZE; ++i) {
                lookupTable[i] = super.name(i);
            }
        }
    }

    static class ArrayEntityMap implements EntityMap {
        protected int growBy = 100;
        protected int size = 0;
        protected String[] names;
        protected int[] values;

        /**
         * 
         */
        public ArrayEntityMap() {
            names = new String[growBy];
            values = new int[growBy];
        }

        /**
         * @param growBy
         */
        public ArrayEntityMap(int growBy) {
            this.growBy = growBy;
            names = new String[growBy];
            values = new int[growBy];
        }

        public void add(String name, int value) {
            ensureCapacity(size + 1);
            names[size] = name;
            values[size] = value;
            size++;
        }

        protected void ensureCapacity(int capacity) {
            if (capacity > names.length) {
                int newSize = Math.max(capacity, size + growBy);
                String[] newNames = new String[newSize];
                System.arraycopy(names, 0, newNames, 0, size);
                names = newNames;
                int[] newValues = new int[newSize];
                System.arraycopy(values, 0, newValues, 0, size);
                values = newValues;
            }
        }

        public String name(int value) {
            for (int i = 0; i < size; ++i) {
                if (values[i] == value) {
                    return names[i];
                }
            }
            return null;
        }

        public int value(String name) {
            for (int i = 0; i < size; ++i) {
                if (names[i].equals(name)) {
                    return values[i];
                }
            }
            return -1;
        }
    }

    static class BinaryEntityMap extends ArrayEntityMap {

        /**
         * 
         */
        public BinaryEntityMap() {
        }

        /**
         * @param growBy
         */
        public BinaryEntityMap(int growBy) {
            super(growBy);
        }

        // based on code in java.util.Arrays
        private int binarySearch(int key) {
            int low = 0;
            int high = size - 1;

            while (low <= high) {
                int mid = (low + high) >> 1;
                int midVal = values[mid];

                if (midVal < key)
                    low = mid + 1;
                else if (midVal > key)
                    high = mid - 1;
                else
                    return mid; // key found
            }
            return -(low + 1);  // key not found.
        }

        public void add(String name, int value) {
            ensureCapacity(size + 1);
            int insertAt = binarySearch(value);
            if (insertAt > 0) return;    // note: this means you can't insert the same value twice
            insertAt = -(insertAt + 1);  // binarySearch returns it negative and off-by-one
            System.arraycopy(values, insertAt, values, insertAt + 1, size - insertAt);
            values[insertAt] = value;
            System.arraycopy(names, insertAt, names, insertAt + 1, size - insertAt);
            names[insertAt] = name;
            size++;
        }

        public String name(int value) {
            int index = binarySearch(value);
            if (index < 0) return null;
            return names[index];
        }
    }

    // package scoped for testing
    EntityMap map = new Entities.LookupEntityMap();

    /**
     * @param entityArray
     */
    public void addEntities(String[][] entityArray) {
        for (int i = 0; i < entityArray.length; ++i) {
            addEntity(entityArray[i][0], Integer.parseInt(entityArray[i][1]));
        }
    }

    /**
     * @param name
     * @param value
     */
    public void addEntity(String name, int value) {
        map.add(name, value);
    }

    /**
     * @param value
     * @return the string
     */
    public String entityName(int value) {
        return map.name(value);
    }


    /**
     * @param name
     * @return the number
     */
    public int entityValue(String name) {
        return map.value(name);
    }

    /**
     * <p>Escapes the characters in a <code>String</code>.</p>
     *
     * <p>For example, if you have called addEntity(&quot;foo&quot;, 0xA1),
     * escape(&quot;\u00A1&quot;) will return &quot;&amp;foo;&quot;</p>
     *
     * @param str The <code>String</code> to escape.
     * @return A new escaped <code>String</code>.
     */
    public String escape(String str) {
        //todo: rewrite to use a Writer
        StringBuffer buf = new StringBuffer(str.length() * 2);
        int i;
        for (i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            String entityName = this.entityName(ch);
            if (entityName == null) {
                if (ch > 0x7F) {
                    int intValue = ch;
                    buf.append("&#"); //$NON-NLS-1$
                    buf.append(intValue);
                    buf.append(';');
                } else {
                    buf.append(ch);
                }
            } else {
                buf.append('&');
                buf.append(entityName);
                buf.append(';');
            }
        }
        return buf.toString();
    }

    /**
     * <p>Unescapes the entities in a <code>String</code>.</p>
     *
     * <p>For example, if you have called addEntity(&quot;foo&quot;, 0xA1),
     * unescape(&quot;&amp;foo;&quot;) will return &quot;\u00A1&quot;</p>
     *
     * @param str The <code>String</code> to escape.
     * @return A new escaped <code>String</code>.
     */
    public String unescape(String str) {
        StringBuffer buf = new StringBuffer(str.length());
        int i;
        for (i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '&') {
                int semi = str.indexOf(';', i + 1);
                if (semi == -1) {
                    buf.append(ch);
                    continue;
                }
                String entityName = str.substring(i + 1, semi);
                int entityValue;
                if (entityName.charAt(0) == '#') {
                    entityValue = Integer.parseInt(entityName.substring(1));
                } else {
                    entityValue = this.entityValue(entityName);
                }
                if (entityValue == -1) {
                    buf.append('&');
                    buf.append(entityName);
                    buf.append(';');
                } else {
                    buf.append((char) (entityValue));
                }
                i = semi;
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

}
