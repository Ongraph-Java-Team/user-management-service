package com.ongraph.usermanagementapp.util;

import org.apache.commons.lang3.ArrayUtils;

public class LoggerHelper {

private static final String SEPARATOR = "\r\n";
private static final String CAUSE_CAPTION = "Caused by: ";
private static final String SUPPRESSED_CAPTION = "Suppressed: ";

/**
 * The first 10 lines of exception stack information are returned by default
 *
 * @param e
 * @return
 */
public static String printStackTrace(Throwable e) {
    if (e == null) {
        return "";
    }
    return printStackTrace(e, 5);
}

public static String printStackTrace(Throwable e, int maxLineCount) {
    if (e == null || maxLineCount <= 0) {
        return "";
    }
    StringBuilder sb = new StringBuilder(maxLineCount * 10);
    sb.append(e.toString()).append(SEPARATOR);
    StackTraceElement[] trace = e.getStackTrace();
    if (trace == null) {
        return e.toString();
    }
    int count = maxLineCount > trace.length ? trace.length : maxLineCount;
    int framesInCommon = trace.length - count;
    for (int i = 0; i < count; i++) {
        sb.append("\tat ").append(trace[i]).append(SEPARATOR);
    }
    if (framesInCommon != 0) {
        sb.append("\t... ").append(framesInCommon).append(" more").append(SEPARATOR);
    }
    // Print suppressed exceptions, if any
    Throwable[] suppressedExceptions = e.getSuppressed();
    if (ArrayUtils.isNotEmpty(suppressedExceptions)) {
        for (Throwable suppressedException : suppressedExceptions) {
            sb.append(printEnclosedStackTrace(suppressedException, maxLineCount, trace, SUPPRESSED_CAPTION, "\t"));
        }
    }
    // Print cause, if any
    Throwable cause = e.getCause();
    if (cause != null) {
        sb.append(printEnclosedStackTrace(cause, maxLineCount, trace, CAUSE_CAPTION, ""));
    }
    return sb.toString();
}

private static String printEnclosedStackTrace(Throwable e, int maxLineCount, StackTraceElement[] enclosingTrace,
                                              String caption, String prefix) {
    StringBuilder sb = new StringBuilder(maxLineCount * 5);
    StackTraceElement[] trace = e.getStackTrace();
    int m = trace.length - 1;
    int n = enclosingTrace.length - 1;
    while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
        m--;
        n--;
    }
    int count = maxLineCount > (m + 1) ? (m + 1) : maxLineCount;
    int framesInCommon = trace.length - count;
    // Print our stack trace
    sb.append(prefix).append(caption).append(e.toString()).append(SEPARATOR);
    for (int i = 0; i < count; i++) {
        sb.append(prefix).append("\tat ").append(trace[i]).append(SEPARATOR);
    }
    if (framesInCommon != 0) {
        sb.append(prefix).append("\t... ").append(framesInCommon).append(" more").append(SEPARATOR);
    }
    // Print suppressed exceptions, if any
    Throwable[] suppressedExceptions = e.getSuppressed();
    if (ArrayUtils.isNotEmpty(suppressedExceptions)) {
        for (Throwable suppressedException : suppressedExceptions) {
            sb.append(printEnclosedStackTrace(suppressedException, maxLineCount, trace, SUPPRESSED_CAPTION, prefix + "\t"));
        }
    }
    // Print cause, if any
    Throwable cause = e.getCause();
    if (cause != null) {
        sb.append(printEnclosedStackTrace(cause, maxLineCount, trace, CAUSE_CAPTION, prefix));
    }
    return sb.toString();
 }
}