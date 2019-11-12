package zs.qimai.com.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnimation2 {
    String id();
    String name();
}
