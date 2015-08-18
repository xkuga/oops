package com.onepiece.kuga.crypto;

import java.util.Random;
import java.nio.charset.Charset;

import android.util.Base64;

public class Aes {

    /**
     * 密码字节
     */
    private byte[] password;

    /**
     * 盐的字节数
     */
    private int saltByteNum;

    /**
     * 密钥的位数
     */
    private int keySize;

    /**
     * 密钥字节数
     */
    private int keyByteNum;

    /**
     * 密钥矩阵的列数(行固定为 4)
     */
    private int keyColumn;

    /**
     * 加密的轮次
     */
    private int encryptRound;

    /**
     * 扩展密钥后的子密钥个数
     */
    private int subKeyNum;

    /**
     * 扩展密钥后的子密钥矩阵总列数(行固定为 4)
     */
    private int subKeyAllColumn;

    /**
     * Aes 处理的块字节数
     */
    private int blockByteNum;

    /**
     * Aes 采用的编码
     */
    private Charset charset;

    /**
     * Aes 分块的位数
     */
    public static final int BLOCK_SIZE = 128;

    /**
     * 默认的密钥位数
     */
    public static final int DEFAULT_KEY_SIZE = 256;

    /**
     * 默认的盐的字节数
     */
    public static final int DEFAULT_SALT_BYTE_NUM = 32;

    /**
     * 字节替代表
     */
    private static byte[] SBOX = {
              99,  124,  119,  123,  -14,  107,  111,  -59,   48,    1,  103,   43,   -2,  -41,  -85,  118,
             -54, -126,  -55,  125,   -6,   89,   71,  -16,  -83,  -44,  -94,  -81, -100,  -92,  114,  -64,
             -73,   -3, -109,   38,   54,   63,   -9,  -52,   52,  -91,  -27,  -15,  113,  -40,   49,   21,
               4,  -57,   35,  -61,   24, -106,    5, -102,    7,   18, -128,  -30,  -21,   39,  -78,  117,
               9, -125,   44,   26,   27,  110,   90,  -96,   82,   59,  -42,  -77,   41,  -29,   47, -124,
              83,  -47,    0,  -19,   32,   -4,  -79,   91,  106,  -53,  -66,   57,   74,   76,   88,  -49,
             -48,  -17,  -86,   -5,   67,   77,   51, -123,   69,   -7,    2,  127,   80,   60,  -97,  -88,
              81,  -93,   64, -113, -110,  -99,   56,  -11,  -68,  -74,  -38,   33,   16,   -1,  -13,  -46,
             -51,   12,   19,  -20,   95, -105,   68,   23,  -60,  -89,  126,   61,  100,   93,   25,  115,
              96, -127,   79,  -36,   34,   42, -112, -120,   70,  -18,  -72,   20,  -34,   94,   11,  -37,
             -32,   50,   58,   10,   73,    6,   36,   92,  -62,  -45,  -84,   98, -111, -107,  -28,  121,
             -25,  -56,   55,  109, -115,  -43,   78,  -87,  108,   86,  -12,  -22,  101,  122,  -82,    8,
             -70,  120,   37,   46,   28,  -90,  -76,  -58,  -24,  -35,  116,   31,   75,  -67, -117, -118,
             112,   62,  -75,  102,   72,    3,  -10,   14,   97,   53,   87,  -71, -122,  -63,   29,  -98,
             -31,   -8, -104,   17,  105,  -39, -114, -108, -101,   30, -121,  -23,  -50,   85,   40,  -33,
            -116,  -95, -119,   13,  -65,  -26,   66,  104,   65, -103,   45,   15,  -80,   84,  -69,   22
    };

    /**
     * 字节代替的逆表
     */
    private static byte[] INV_SBOX = {
              82,    9,  106,  -43,   48,   54,  -91,   56,  -65,   64,  -93,  -98, -127,  -13,  -41,   -5,
             124,  -29,   57, -126, -101,   47,   -1, -121,   52, -114,   67,   68,  -60,  -34,  -23,  -53,
              84,  123, -108,   50,  -90,  -62,   35,   61,  -18,   76, -107,   11,   66,   -6,  -61,   78,
               8,   46,  -95,  102,   40,  -39,   36,  -78,  118,   91,  -94,   73,  109, -117,  -47,   37,
             114,   -8,  -10,  100, -122,  104, -104,   22,  -44,  -92,   92,  -52,   93,  101,  -74, -110,
             108,  112,   72,   80,   -3,  -19,  -71,  -38,   94,   21,   70,   87,  -89, -115,  -99, -124,
            -112,  -40,  -85,    0, -116,  -68,  -45,   10,   -9,  -28,   88,    5,  -72,  -77,   69,    6,
             -48,   44,   30, -113,  -54,   63,   15,    2,  -63,  -81,  -67,    3,    1,   19, -118,  107,
              58, -111,   17,   65,   79,  103,  -36,  -22, -105,  -14,  -49,  -50,  -16,  -76,  -26,  115,
            -106,  -84,  116,   34,  -25,  -83,   53, -123,  -30,   -7,   55,  -24,   28,  117,  -33,  110,
              71,  -15,   26,  113,   29,   41,  -59, -119,  111,  -73,   98,   14,  -86,   24,  -66,   27,
              -4,   86,   62,   75,  -58,  -46,  121,   32, -102,  -37,  -64,   -2,  120,  -51,   90,  -12,
              31,  -35,  -88,   51, -120,    7,  -57,   49,  -79,   18,   16,   89,   39, -128,  -20,   95,
              96,   81,  127,  -87,   25,  -75,   74,   13,   45,  -27,  122,  -97, -109,  -55, -100,  -17,
             -96,  -32,   59,   77,  -82,   42,  -11,  -80,  -56,  -21,  -69,   60, -125,   83, -103,   97,
              23,   43,    4,  126,  -70,  119,  -42,   38,  -31,  105,   20,   99,   85,   33,   12,  125
    };

    /**
     * 有限域中的乘 2 运算结果表
     */
    private static byte[] MUL_BY_2 = {
               0,    2,    4,    6,    8,   10,   12,   14,   16,   18,   20,   22,   24,   26,   28,   30,
              32,   34,   36,   38,   40,   42,   44,   46,   48,   50,   52,   54,   56,   58,   60,   62,
              64,   66,   68,   70,   72,   74,   76,   78,   80,   82,   84,   86,   88,   90,   92,   94,
              96,   98,  100,  102,  104,  106,  108,  110,  112,  114,  116,  118,  120,  122,  124,  126,
            -128, -126, -124, -122, -120, -118, -116, -114, -112, -110, -108, -106, -104, -102, -100,  -98,
             -96,  -94,  -92,  -90,  -88,  -86,  -84,  -82,  -80,  -78,  -76,  -74,  -72,  -70,  -68,  -66,
             -64,  -62,  -60,  -58,  -56,  -54,  -52,  -50,  -48,  -46,  -44,  -42,  -40,  -38,  -36,  -34,
             -32,  -30,  -28,  -26,  -24,  -22,  -20,  -18,  -16,  -14,  -12,  -10,   -8,   -6,   -4,   -2,
              27,   25,   31,   29,   19,   17,   23,   21,   11,    9,   15,   13,    3,    1,    7,    5,
              59,   57,   63,   61,   51,   49,   55,   53,   43,   41,   47,   45,   35,   33,   39,   37,
              91,   89,   95,   93,   83,   81,   87,   85,   75,   73,   79,   77,   67,   65,   71,   69,
             123,  121,  127,  125,  115,  113,  119,  117,  107,  105,  111,  109,   99,   97,  103,  101,
            -101, -103,  -97,  -99, -109, -111, -105, -107, -117, -119, -113, -115, -125, -127, -121, -123,
             -69,  -71,  -65,  -67,  -77,  -79,  -73,  -75,  -85,  -87,  -81,  -83,  -93,  -95,  -89,  -91,
             -37,  -39,  -33,  -35,  -45,  -47,  -41,  -43,  -53,  -55,  -49,  -51,  -61,  -63,  -57,  -59,
              -5,   -7,   -1,   -3,  -13,  -15,   -9,  -11,  -21,  -23,  -17,  -19,  -29,  -31,  -25,  -27
    };

    /**
     * 有限域中的乘 4 运算结果表
     */
    private static byte[] MUL_BY_3 = {
               0,    3,    6,    5,   12,   15,   10,    9,   24,   27,   30,   29,   20,   23,   18,   17,
              48,   51,   54,   53,   60,   63,   58,   57,   40,   43,   46,   45,   36,   39,   34,   33,
              96,   99,  102,  101,  108,  111,  106,  105,  120,  123,  126,  125,  116,  119,  114,  113,
              80,   83,   86,   85,   92,   95,   90,   89,   72,   75,   78,   77,   68,   71,   66,   65,
             -64,  -61,  -58,  -59,  -52,  -49,  -54,  -55,  -40,  -37,  -34,  -35,  -44,  -41,  -46,  -47,
             -16,  -13,  -10,  -11,   -4,   -1,   -6,   -7,  -24,  -21,  -18,  -19,  -28,  -25,  -30,  -31,
             -96,  -93,  -90,  -91,  -84,  -81,  -86,  -87,  -72,  -69,  -66,  -67,  -76,  -73,  -78,  -79,
            -112, -109, -106, -107, -100,  -97, -102, -103, -120, -117, -114, -115, -124, -121, -126, -127,
            -101, -104,  -99,  -98, -105, -108, -111, -110, -125, -128, -123, -122, -113, -116, -119, -118,
             -85,  -88,  -83,  -82,  -89,  -92,  -95,  -94,  -77,  -80,  -75,  -74,  -65,  -68,  -71,  -70,
              -5,   -8,   -3,   -2,   -9,  -12,  -15,  -14,  -29,  -32,  -27,  -26,  -17,  -20,  -23,  -22,
             -53,  -56,  -51,  -50,  -57,  -60,  -63,  -62,  -45,  -48,  -43,  -42,  -33,  -36,  -39,  -38,
              91,   88,   93,   94,   87,   84,   81,   82,   67,   64,   69,   70,   79,   76,   73,   74,
             107,  104,  109,  110,  103,  100,   97,   98,  115,  112,  117,  118,  127,  124,  121,  122,
              59,   56,   61,   62,   55,   52,   49,   50,   35,   32,   37,   38,   47,   44,   41,   42,
              11,    8,   13,   14,    7,    4,    1,    2,   19,   16,   21,   22,   31,   28,   25,   26
    };

    /**
     * 有限域中的乘 9 运算结果表
     */
    private static byte[] MUL_BY_9 = {
               0,    9,   18,   27,   36,   45,   54,   63,   72,   65,   90,   83,  108,  101,  126,  119,
            -112, -103, -126, -117,  -76,  -67,  -90,  -81,  -40,  -47,  -54,  -61,   -4,  -11,  -18,  -25,
              59,   50,   41,   32,   31,   22,   13,    4,  115,  122,   97,  104,   87,   94,   69,   76,
             -85,  -94,  -71,  -80, -113, -122,  -99, -108,  -29,  -22,  -15,   -8,  -57,  -50,  -43,  -36,
             118,  127,  100,  109,   82,   91,   64,   73,   62,   55,   44,   37,   26,   19,    8,    1,
             -26,  -17,  -12,   -3,  -62,  -53,  -48,  -39,  -82,  -89,  -68,  -75, -118, -125, -104, -111,
              77,   68,   95,   86,  105,   96,  123,  114,    5,   12,   23,   30,   33,   40,   51,   58,
             -35,  -44,  -49,  -58,   -7,  -16,  -21,  -30, -107, -100, -121, -114,  -79,  -72,  -93,  -86,
             -20,  -27,   -2,   -9,  -56,  -63,  -38,  -45,  -92,  -83,  -74,  -65, -128, -119, -110, -101,
             124,  117,  110,  103,   88,   81,   74,   67,   52,   61,   38,   47,   16,   25,    2,   11,
             -41,  -34,  -59,  -52,  -13,   -6,  -31,  -24,  -97, -106, -115, -124,  -69,  -78,  -87,  -96,
              71,   78,   85,   92,   99,  106,  113,  120,   15,    6,   29,   20,   43,   34,   57,   48,
            -102, -109, -120, -127,  -66,  -73,  -84,  -91,  -46,  -37,  -64,  -55,  -10,   -1,  -28,  -19,
              10,    3,   24,   17,   46,   39,   60,   53,   66,   75,   80,   89,  102,  111,  116,  125,
             -95,  -88,  -77,  -70, -123, -116, -105,  -98,  -23,  -32,   -5,  -14,  -51,  -60,  -33,  -42,
              49,   56,   35,   42,   21,   28,    7,   14,  121,  112,  107,   98,   93,   84,   79,   70
    };

    /**
     * 有限域中的乘 11 运算结果表
     */
    private static byte[] MUL_BY_11 = {
               0,   11,   22,   29,   44,   39,   58,   49,   88,   83,   78,   69,  116,  127,   98,  105,
             -80,  -69,  -90,  -83, -100, -105, -118, -127,  -24,  -29,   -2,  -11,  -60,  -49,  -46,  -39,
             123,  112,  109,  102,   87,   92,   65,   74,   35,   40,   53,   62,   15,    4,   25,   18,
             -53,  -64,  -35,  -42,  -25,  -20,  -15,   -6, -109, -104, -123, -114,  -65,  -76,  -87,  -94,
             -10,   -3,  -32,  -21,  -38,  -47,  -52,  -57,  -82,  -91,  -72,  -77, -126, -119, -108,  -97,
              70,   77,   80,   91,  106,   97,  124,  119,   30,   21,    8,    3,   50,   57,   36,   47,
            -115, -122, -101, -112,  -95,  -86,  -73,  -68,  -43,  -34,  -61,  -56,   -7,  -14,  -17,  -28,
              61,   54,   43,   32,   17,   26,    7,   12,  101,  110,  115,  120,   73,   66,   95,   84,
              -9,   -4,  -31,  -22,  -37,  -48,  -51,  -58,  -81,  -92,  -71,  -78, -125, -120, -107,  -98,
              71,   76,   81,   90,  107,   96,  125,  118,   31,   20,    9,    2,   51,   56,   37,   46,
            -116, -121, -102, -111,  -96,  -85,  -74,  -67,  -44,  -33,  -62,  -55,   -8,  -13,  -18,  -27,
              60,   55,   42,   33,   16,   27,    6,   13,  100,  111,  114,  121,   72,   67,   94,   85,
               1,   10,   23,   28,   45,   38,   59,   48,   89,   82,   79,   68,  117,  126,   99,  104,
             -79,  -70,  -89,  -84,  -99, -106, -117, -128,  -23,  -30,   -1,  -12,  -59,  -50,  -45,  -40,
             122,  113,  108,  103,   86,   93,   64,   75,   34,   41,   52,   63,   14,    5,   24,   19,
             -54,  -63,  -36,  -41,  -26,  -19,  -16,   -5, -110, -103, -124, -113,  -66,  -75,  -88,  -93
    };

    /**
     * 有限域中的乘 13 运算结果表
     */
    private static byte[] MUL_BY_13 = {
               0,   13,   26,   23,   52,   57,   46,   35,  104,  101,  114,  127,   92,   81,   70,   75,
             -48,  -35,  -54,  -57,  -28,  -23,   -2,  -13,  -72,  -75,  -94,  -81, -116, -127, -106, -101,
             -69,  -74,  -95,  -84, -113, -126, -107, -104,  -45,  -34,  -55,  -60,  -25,  -22,   -3,  -16,
             107,  102,  113,  124,   95,   82,   69,   72,    3,   14,   25,   20,   55,   58,   45,   32,
             109,   96,  119,  122,   89,   84,   67,   78,    5,    8,   31,   18,   49,   60,   43,   38,
             -67,  -80,  -89,  -86, -119, -124, -109,  -98,  -43,  -40,  -49,  -62,  -31,  -20,   -5,  -10,
             -42,  -37,  -52,  -63,  -30,  -17,   -8,  -11,  -66,  -77,  -92,  -87, -118, -121, -112,  -99,
               6,   11,   28,   17,   50,   63,   40,   37,  110,   99,  116,  121,   90,   87,   64,   77,
             -38,  -41,  -64,  -51,  -18,  -29,  -12,   -7,  -78,  -65,  -88,  -91, -122, -117, -100, -111,
              10,    7,   16,   29,   62,   51,   36,   41,   98,  111,  120,  117,   86,   91,   76,   65,
              97,  108,  123,  118,   85,   88,   79,   66,    9,    4,   19,   30,   61,   48,   39,   42,
             -79,  -68,  -85,  -90, -123, -120,  -97, -110,  -39,  -44,  -61,  -50,  -19,  -32,   -9,   -6,
             -73,  -70,  -83,  -96, -125, -114, -103, -108,  -33,  -46,  -59,  -56,  -21,  -26,  -15,   -4,
             103,  106,  125,  112,   83,   94,   73,   68,   15,    2,   21,   24,   59,   54,   33,   44,
              12,    1,   22,   27,   56,   53,   34,   47,  100,  105,  126,  115,   80,   93,   74,   71,
             -36,  -47,  -58,  -53,  -24,  -27,  -14,   -1,  -76,  -71,  -82,  -93, -128, -115, -102, -105
    };

    /**
     * 有限域中的乘 14 运算结果表
     */
    private static byte[] MUL_BY_14 = {
               0,   14,   28,   18,   56,   54,   36,   42,  112,  126,  108,   98,   72,   70,   84,   90,
             -32,  -18,   -4,  -14,  -40,  -42,  -60,  -54, -112,  -98, -116, -126,  -88,  -90,  -76,  -70,
             -37,  -43,  -57,  -55,  -29,  -19,   -1,  -15,  -85,  -91,  -73,  -71, -109,  -99, -113, -127,
              59,   53,   39,   41,    3,   13,   31,   17,   75,   69,   87,   89,  115,  125,  111,   97,
             -83,  -93,  -79,  -65, -107, -101, -119, -121,  -35,  -45,  -63,  -49,  -27,  -21,   -7,   -9,
              77,   67,   81,   95,  117,  123,  105,  103,   61,   51,   33,   47,    5,   11,   25,   23,
             118,  120,  106,  100,   78,   64,   82,   92,    6,    8,   26,   20,   62,   48,   34,   44,
            -106, -104, -118, -124,  -82,  -96,  -78,  -68,  -26,  -24,   -6,  -12,  -34,  -48,  -62,  -52,
              65,   79,   93,   83,  121,  119,  101,  107,   49,   63,   45,   35,    9,    7,   21,   27,
             -95,  -81,  -67,  -77, -103, -105, -123, -117,  -47,  -33,  -51,  -61,  -23,  -25,  -11,   -5,
            -102, -108, -122, -120,  -94,  -84,  -66,  -80,  -22,  -28,  -10,   -8,  -46,  -36,  -50,  -64,
             122,  116,  102,  104,   66,   76,   94,   80,   10,    4,   22,   24,   50,   60,   46,   32,
             -20,  -30,  -16,   -2,  -44,  -38,  -56,  -58, -100, -110, -128, -114,  -92,  -86,  -72,  -74,
              12,    2,   16,   30,   52,   58,   40,   38,  124,  114,   96,  110,   68,   74,   88,   86,
              55,   57,   43,   37,   15,    1,   19,   29,   71,   73,   91,   85,  127,  113,   99,  109,
             -41,  -39,  -53,  -59,  -17,  -31,  -13,   -3,  -89,  -87,  -69,  -75,  -97, -111, -125, -115
    };

    /**
     * 有限域中的 2^(i-1) 次方幂表
     */
    private static byte[] RCON = {
            -115,    1,    2,    4,    8,   16,   32,   64, -128,   27,   54,  108,  -40,  -85,   77, -102,
              47,   94,  -68,   99,  -58, -105,   53,  106,  -44,  -77,  125,   -6,  -17,  -59, -111,   57,
             114,  -28,  -45,  -67,   97,  -62,  -97,   37,   74, -108,   51,  102,  -52, -125,   29,   58,
             116,  -24,  -53, -115,    1,    2,    4,    8,   16,   32,   64, -128,   27,   54,  108,  -40,
             -85,   77, -102,   47,   94,  -68,   99,  -58, -105,   53,  106,  -44,  -77,  125,   -6,  -17,
             -59, -111,   57,  114,  -28,  -45,  -67,   97,  -62,  -97,   37,   74, -108,   51,  102,  -52,
            -125,   29,   58,  116,  -24,  -53, -115,    1,    2,    4,    8,   16,   32,   64, -128,   27,
              54,  108,  -40,  -85,   77, -102,   47,   94,  -68,   99,  -58, -105,   53,  106,  -44,  -77,
             125,   -6,  -17,  -59, -111,   57,  114,  -28,  -45,  -67,   97,  -62,  -97,   37,   74, -108,
              51,  102,  -52, -125,   29,   58,  116,  -24,  -53, -115,    1,    2,    4,    8,   16,   32,
              64, -128,   27,   54,  108,  -40,  -85,   77, -102,   47,   94,  -68,   99,  -58, -105,   53,
             106,  -44,  -77,  125,   -6,  -17,  -59, -111,   57,  114,  -28,  -45,  -67,   97,  -62,  -97,
              37,   74, -108,   51,  102,  -52, -125,   29,   58,  116,  -24,  -53, -115,    1,    2,    4,
               8,   16,   32,   64, -128,   27,   54,  108,  -40,  -85,   77, -102,   47,   94,  -68,   99,
             -58, -105,   53,  106,  -44,  -77,  125,   -6,  -17,  -59, -111,   57,  114,  -28,  -45,  -67,
              97,  -62,  -97,   37,   74, -108,   51,  102,  -52, -125,   29,   58,  116,  -24,  -53, -115
    };

    /**
     * 指定密码构建 Aes 对象
     *
     * @param password 密码
     */
    public Aes(String password) {
        this(password, DEFAULT_KEY_SIZE, DEFAULT_SALT_BYTE_NUM, Charset.forName("UTF-8"));
    }

    /**
     * 指定密码，密钥位数构建 Aes 对象
     *
     * @param password 密码
     * @param keySize 密钥大小
     */
    public Aes(String password, int keySize) {
        this(password, keySize, DEFAULT_SALT_BYTE_NUM, Charset.forName("UTF-8"));
    }

    /**
     * 指定密码，密钥位数，盐字节数构建 Aes 对象
     *
     * @param password 密码
     * @param keySize 密钥大小
     * @param saltByteNum 盐的字节数
     */
    public Aes(String password, int keySize, int saltByteNum) {
        this(password, keySize, saltByteNum, Charset.forName("UTF-8"));
    }

    /**
     * 指定密码，密钥位数，盐字节数，编码构建 Aes 对象
     *
     * @param password 密码
     * @param keySize 密仴位数
     * @param saltByteNum 盐的字节数
     * @param charset 编码
     */
    public Aes(String password, int keySize, int saltByteNum, Charset charset) {
        this.charset = charset;
        this.password = password.getBytes(charset);
        this.keySize = keySize;
        this.saltByteNum = saltByteNum;
        this.keyByteNum = keySize / 8;
        this.keyColumn = keyByteNum / 4;
        this.encryptRound = keySize == 256 ? 14 : keySize == 192 ? 12 : 10;
        this.subKeyNum = (encryptRound + 1);
        this.subKeyAllColumn = 4 * subKeyNum;
        this.blockByteNum = BLOCK_SIZE / 8;
    }

    /**
     * 加密明文串，返回的密文串用 Base64 编码
     *
     * @param plaintext 明文
     * @return 密文
     */
    public String encrypt(String plaintext) {
        byte[] encryptedBytes = encrypt(plaintext.getBytes(charset));
        return new String(Base64.encode(encryptedBytes, Base64.DEFAULT), charset);
    }

    /**
     * 字节流加密
     *
     * @param stream 字节流
     * @return 加密流
     */
    public byte[] encrypt(byte[] stream) {
        byte[] salt = new byte[saltByteNum];
        byte[] key = new byte[keyByteNum];
        byte[] iv = new byte[blockByteNum];

        // 盐为随机字节流
        new Random().nextBytes(salt);

        // 从 password 和 salt 中生成 key 和 iv
        genKeyAndIV(password, salt, key, iv);

        // 扩展密钥并获取子密钥
        byte[] expandKey = keyExpansion(key);
        byte[][][] subKey = new byte[subKeyNum][][];

        // 16 个字节为一个子密钥，并把子密钥转为矩阵状态
        for (int i = 0; i < subKey.length; i++) {
            subKey[i] = byteArrayToMatrix(subByteArray(expandKey, 16 * i, 16));
        }

        // 调整字节流，进行填充处理
        byte[] paddingStream = paddingStream(stream);

        // 状态矩阵
        byte[][][] state = new byte[paddingStream.length / 16][][];

        // 把字节流放到状态矩阵
        for (int i = 0; i < state.length; i++) {
            state[i] = byteArrayToMatrix(subByteArray(paddingStream, 16 * i, 16));
        }

        // 把 iv 转为矩阵状态
        byte[][] ivState = byteArrayToMatrix(iv);

        // 以 CBC 模式对每一个状态矩阵进行加密
        xor(state[0], ivState);
        encryptUnit(state[0], subKey);

        for (int i = 1; i < state.length; i++) {
            xor(state[i], state[i - 1]);
            encryptUnit(state[i], subKey);
        }

        // 加密后的字节数组
        byte[] encryptedBytes = new byte[paddingStream.length];

        // 把加密后的状态矩阵放到加密字节数组中
        for (int i = 0; i < state.length; i++) {
            System.arraycopy(matrixToByteArray(state[i]), 0, encryptedBytes, 16 * i, 16);
        }

        // print(encryptedBytes);

        // 返回盐和加密后的字节数组
        return mergeByteArray(salt, encryptedBytes);
    }

    /**
     * 加密单元
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     * @param subKey 各轮子密钥
     */
    private void encryptUnit(byte[][] state, byte[][][] subKey) {
        // 状态与初始密钥作首轮异或
        addRoundKey(state, subKey[0]);

        // 根据不同的密钥位数作不同的轮次加密
        for (int i = 1; i < encryptRound; i++) {
            subBytes(state);    // 字节替代
            shiftRows(state);   // 行移动
            mixColumns(state);  // 列混淆
            addRoundKey(state, subKey[i]);  // 与子密钥异或
        }

        // 最后一轮不作列混淆，所以要单放出来
        subBytes(state);
        shiftRows(state);
        addRoundKey(state, subKey[encryptRound]);
    }

    /**
     * 状态矩阵与密钥状态矩阵异或
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     * @param keyState 密钥状态矩阵
     */
    private void addRoundKey(byte[][] state, byte[][] keyState) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                state[r][c] ^= keyState[r][c];
            }
        }
    }

    /**
     * 对状态矩阵进行字节替换，即有限域的逆元代替与仿射变换过程
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     */
    private void subBytes(byte[][] state) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                state[r][c] = SBOX[0xff & state[r][c]];
            }
        }
    }

    /**
     * 对状态矩阵进行行移动，第 0 行不动，第 1 行向左移 1 字节，以此类推
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     */
    private void shiftRows(byte[][] state) {
        byte[] t = new byte[4];

        for (int r = 1; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                t[c] = state[r][(r + c) % 4];
            }
            System.arraycopy(t, 0, state[r], 0, 4);
        }
    }

    /**
     * 对状态矩阵进行列混淆，即与一个特定矩阵相乘的过程
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     */
    private void mixColumns(byte[][] state) {
        byte[] a = new byte[4];

        for (int c = 0; c < 4; c++) {
            // a保存状态的一列
            for (int r = 0; r < 4; r++) {
                a[r] = state[r][c];
            }

            int t0 = 0xff & a[0], t1 = 0xff & a[1], t2 = 0xff & a[2], t3 = 0xff & a[3];

            // state[0][c] = 2 * a[0] + 3 * a[1] + a[2] + a[3]
            // 以此类推...在这里+是异或操作，乘已用表优化
            state[0][c] = (byte) (MUL_BY_2[t0] ^ MUL_BY_3[t1] ^ a[2] ^ a[3]);
            state[1][c] = (byte) (a[0] ^ MUL_BY_2[t1] ^ MUL_BY_3[t2] ^ a[3]);
            state[2][c] = (byte) (a[0] ^ a[1] ^ MUL_BY_2[t2] ^ MUL_BY_3[t3]);
            state[3][c] = (byte) (MUL_BY_3[t0] ^ a[1] ^ a[2] ^ MUL_BY_2[t3]);
        }
    }

    /**
     * 解密密文串
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public String decrypt(String ciphertext) {
        try {
            return new String(decrypt(Base64.decode(ciphertext, Base64.DEFAULT)), charset);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字节流解密
     *
     * @param stream 字节流
     * @return 解密流
     */
    public byte[] decrypt(byte[] stream) {
        byte[] salt = subByteArray(stream, 0, saltByteNum);
        byte[] key = new byte[keyByteNum];
        byte[] iv = new byte[blockByteNum];

        // 从 password 和 salt 中生成 key 和 iv
        genKeyAndIV(password, salt, key, iv);

        // 扩展密钥并获取子密钥
        byte[] expandKey = keyExpansion(key);
        byte[][][] subKey = new byte[subKeyNum][][];

        // 16 个字节为一个子密钥，并把子密钥转为矩阵状态
        for (int i = 0; i < subKey.length; i++) {
            subKey[i] = byteArrayToMatrix(subByteArray(expandKey, 16 * i, 16));
        }

        // 截取内容部分
        byte[] content = subByteArray(stream, saltByteNum, stream.length - saltByteNum);

        // 状态矩阵
        byte[][][] state = new byte[content.length / 16][][];

        // 把内容字节流放到状态矩阵
        for (int i = 0; i < state.length; i++) {
            state[i] = byteArrayToMatrix(subByteArray(content, 16 * i, 16));
        }

        // 把 iv 转为矩阵状态
        byte[][] ivState = byteArrayToMatrix(iv);

        // 以 CBC 模式从后面对每一个状态矩阵进行解密
        for (int i = state.length - 1; i > 0; i--) {
            decryptUnit(state[i], subKey);
            xor(state[i], state[i - 1]);
        }

        decryptUnit(state[0], subKey);
        xor(state[0], ivState);

        // 解密后的字节数组
        byte[] decryptedBytes = new byte[content.length];

        // 把解密后的状态矩阵放到解密字节数组中
        for (int i = 0; i < state.length; i++) {
            System.arraycopy(matrixToByteArray(state[i]), 0, decryptedBytes, 16 * i, 16);
        }

        // 去掉加密时填充的字节并返回结果
        return subByteArray(decryptedBytes, 0, decryptedBytes.length - decryptedBytes[decryptedBytes.length - 1]);
    }

    /**
     * 解密单元
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     * @param subKey 各轮子密钥
     */
    private void decryptUnit(byte[][] state, byte[][][] subKey) {
        // 全部参照上面的加密反过来写就可以了
        addRoundKey(state, subKey[encryptRound]);
        invShiftRows(state);
        invSubBytes(state);

        for (int i = encryptRound - 1; i > 0; i--) {
            addRoundKey(state, subKey[i]);
            invMixColumns(state);
            invShiftRows(state);
            invSubBytes(state);
        }

        addRoundKey(state, subKey[0]);
    }

    /**
     * 状态矩阵字节替代的逆操作
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     */
    private void invSubBytes(byte[][] state) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                state[r][c] = INV_SBOX[0xff & state[r][c]];
            }
        }
    }

    /**
     * 状态矩阵行移动的逆操作
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     */
    private void invShiftRows(byte[][] state) {
        byte[] t = new byte[4];

        for (int r = 1; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                t[(r + c) % 4] = state[r][c];
            }
            System.arraycopy(t, 0, state[r], 0, 4);
        }
    }

    /**
     * 列混淆的逆操作
     *
     * @param state 状态矩阵，即 4*4 的字节矩阵
     */
    private void invMixColumns(byte[][] state) {
        byte[] a = new byte[4];

        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                a[r] = state[r][c];
            }

            int t0 = 0xff & a[0], t1 = 0xff & a[1], t2 = 0xff & a[2], t3 = 0xff & a[3];

            state[0][c] = (byte) (MUL_BY_14[t0] ^ MUL_BY_11[t1] ^ MUL_BY_13[t2] ^ MUL_BY_9[t3]);
            state[1][c] = (byte) (MUL_BY_9[t0] ^ MUL_BY_14[t1] ^ MUL_BY_11[t2] ^ MUL_BY_13[t3]);
            state[2][c] = (byte) (MUL_BY_13[t0] ^ MUL_BY_9[t1] ^ MUL_BY_14[t2] ^ MUL_BY_11[t3]);
            state[3][c] = (byte) (MUL_BY_11[t0] ^ MUL_BY_13[t1] ^ MUL_BY_9[t2] ^ MUL_BY_14[t3]);
        }
    }

    /**
     * 从密码和盐中生成密钥和初始向量
     *
     * @param password 密码
     * @param salt 盐
     * @param key 密钥
     * @param iv 初始向量
     */
    private void genKeyAndIV(byte[] password, byte[] salt, byte[] key, byte[] iv) {
        // byte[] passwordAndSalt = password;  // for test
        byte[] passwordAndSalt = mergeByteArray(password, salt);

        byte[] t1 = Hash.md5(passwordAndSalt);
        byte[] t2 = Hash.md5(mergeByteArray(t1, passwordAndSalt));
        byte[] t3 = Hash.md5(mergeByteArray(t2, passwordAndSalt));

        if (keySize == 128) {
            System.arraycopy(t1, 0, key, 0, 16);
            System.arraycopy(t2, 0, iv, 0, 16);
        } else if (keySize == 256) {
            System.arraycopy(t1, 0, key, 0, 16);
            System.arraycopy(t2, 0, key, 16, 16);
            System.arraycopy(t3, 0, iv, 0, 16);
        } else {
            System.arraycopy(t1, 0, key, 0, 16);
            System.arraycopy(t2, 0, key, 16, 8);
            System.arraycopy(t2, 8, iv, 0, 8);
            System.arraycopy(t3, 0, iv, 8, 8);
        }
    }

    /**
     * 密钥扩展
     *
     * @param key 密钥
     * @return 扩展密钥
     */
    private byte[] keyExpansion(byte[] key) {
        int i, r, c;

        // 扩展密钥的字节矩阵
        byte[][] matrix = new byte[4][subKeyAllColumn];

        // 把原始的密钥直接放到矩阵中，以先列后行的方式存放
        for (c = 0; c < keyColumn; c++) {
            for (r = 0; r < 4; r++) {
                matrix[r][c] = key[4 * c + r];
            }
        }

        byte temp;
        byte[] t = new byte[4];  // t 用于记录字节矩阵的一列

        // 密钥矩阵扩展
        // PS: 对于 128, 192 位密钥，可统一处理，对于 256 位稍不同
        for (i = 1; c < subKeyAllColumn; c++) {
            for (r = 0; r < 4; r++) {
                t[r] = matrix[r][c - 1];  // t 记录 matrix 矩阵的上一列
            }

            if (c % keyColumn == 0) {
                // 把 t 循环左移一字节
                // 然后对 t 的每个字节用 SBOX 作字节替换
                // 最后对 t 的第一个字节与 RCON 异或
                temp = t[0];
                for (r = 0; r < 3; r++) {
                    t[r] = SBOX[0xff & t[(r + 1) % 4]];
                }
                t[3] = SBOX[0xff & temp];
                t[0] ^= RCON[i++ - 1];
            }

            // 如果密钥为 256 位，并且 c % KeyColumn == 4 则把 t 作 SBOX 字节替换
            else if (keySize == 256 && c % keyColumn == 4) {
                for (r = 0; r < 4; r++) {
                    t[r] = SBOX[0xff & t[r]];
                }
            }

            // 最后扩展密钥矩阵的新列 = 矩阵的[c-keyColumn]列 ^ t表示的列
            for (r = 0; r < 4; r++) {
                matrix[r][c] = (byte) (matrix[r][c - keyColumn] ^ t[r]);
            }
        }

        // 扩展密钥
        byte[] expandKey = new byte[16 * subKeyNum];

        // 把扩展后的密钥矩阵以每 16 个字节（4*4矩阵）的方式分配给 expandKey
        for (i = 0; i < subKeyNum; i++) {
            for (c = 0; c < 4; c++) {
                for (r = 0; r < 4; r++) {
                    expandKey[16 * i + 4 * c + r] = matrix[r][4 * i + c];
                }
            }
        }

        // print(expandKey);

        return expandKey;
    }

    /**
     * 填充字节流
     *
     * @param stream 字节流
     * @return 填充后的字节流
     */
    private byte[] paddingStream(byte[] stream) {
        int remainder = stream.length % 16;

        // 若字节数是 16 的倍数则填充 16，否则填充 16-remainder
        byte padding = (byte) (remainder == 0 ? 16 : 16 - remainder);

        byte[] result = new byte[stream.length + padding];

        System.arraycopy(stream, 0, result, 0, stream.length);

        for (int i = stream.length; i < result.length; i++) {
            result[i] = padding;
        }

        return result;
    }

    /**
     * 合并字节数组
     *
     * @param a 字节数组 a
     * @param b 字节数组 b
     * @return 合并后的新字节数组
     */
    private byte[] mergeByteArray(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];

        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

    /**
     * 获取子字节数组
     *
     * @param srcByteArray 源字节数组
     * @param startIndex 起始下标
     * @param length 长度
     * @return 子字节数组
     */
    private byte[] subByteArray(byte[] srcByteArray, int startIndex, int length) {
        byte[] result = new byte[length];

        System.arraycopy(srcByteArray, startIndex, result, 0, length);

        return result;
    }

    /**
     * 把 4*4 的字节矩阵数组转为字节数组
     *
     * @param matrix 字节矩阵
     * @return 字节数组
     */
    private byte[] matrixToByteArray(byte[][] matrix) {
        byte[] byteArray = new byte[16];

        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                byteArray[4 * c + r] = matrix[r][c];
            }
        }

        return byteArray;
    }

    /**
     * 把字节数组转为 4*4 的字节矩阵数组
     *
     * @param byteArray 字节数组
     * @return 字节矩阵数组
     */
    private byte[][] byteArrayToMatrix(byte[] byteArray) {
        byte[][] matrix = new byte[4][4];

        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                matrix[r][c] = byteArray[c * 4 + r];
            }
        }

        return matrix;
    }

    /**
     * 异或 2 个字节矩阵，结果保存在第一个参数
     *
     * @param a 字节矩阵 a
     * @param b 字节矩阵 b
     */
    private void xor(byte[][] a, byte[][] b) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                a[r][c] ^= b[r][c];
            }
        }
    }

    /**
     * 以矩阵的形式打印字节数组，每 16 个字节为一个矩阵
     *
     * @param bytes 字节数组
     */
    public void print(byte[] bytes) {
        for (int i = 0; i < bytes.length / 16; i++) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    System.out.print(Integer.toHexString(0xff & bytes[16 * i + 4 * c + r]) + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}