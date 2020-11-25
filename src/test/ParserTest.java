package test;

import exceptions.ParserException;
import org.junit.jupiter.api.Test;
import parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private void test(String testLine) throws ParserException {
        new Parser().parse(testLine);
    }

    private void testOk(String testLine) {
        assertDoesNotThrow(() -> {
            test(testLine);
        });
    }

    private void testFail(String testLine) {
        var exception = assertThrows(ParserException.class, () -> {
            test(testLine);
        });
    }

    @Test
    void testSimpleCorrect() {
        testOk("int a;");
        testOk("int b;");
        testOk("string line;");
    }

    @Test
    void testSimpleIncorrect() {
        testFail("int");
        testFail("int b");
        testFail("abacd dsd sdada asfasf");
    }

    @Test
    void testNamesCorrect() {
        testOk("int32_t X, y, Z;");
        testOk( "CONST a, BACA, BA_d221;");
        testOk("_CoLoR R, G, B;");
    }

    @Test
    void testNamesIncorrect() {
        testFail("122 a, b;");
        testFail("string+1 integ));");
        testFail("##testing fai(ure");
    }

    @Test
    void testPointers() {
        testOk("int *****a;");
        testOk("String *  ** ** INF, ***v;");

        testFail("size_t **io*id;");
        testFail("int ** $*io;");
    }

    @Test
    void testGroupDeclarations() {
        testOk("int a; String b; hello **c;");
        testOk("int a, c, v; String iop, **fd, *er; hello **world, today; end *test;");

        testFail("int a; int b, int c");
        testFail("test a test b;;");
        testFail("type x, *x, size_t value; type X; new_type Z");

    }
}