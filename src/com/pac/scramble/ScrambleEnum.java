package com.pac.scramble;

/**
 * Created with IntelliJ IDEA.
 * User: paucarter
 * Date: 1/11/13
 * Time: 6:41 PM
 */
public enum ScrambleEnum {
    a(1), b(4), c(4), d(2), e(1), f(4), g(3), h(3), i(1), j(10), k(5), l(2), m(4),
    n(2), o(1), p(4), q(10), r(1), s(1), t(1), u(2), v(5), w(4), x(8), y(3), z(10);

    private int value;

    private ScrambleEnum(int v) {
        this.value = v;
    }


    public String toString() {
        String s = super.toString();
//        if (s.equals("q")) {
//            s += "qu";
//        }
        return s;
    }
}
