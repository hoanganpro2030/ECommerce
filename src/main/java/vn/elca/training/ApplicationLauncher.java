package vn.elca.training;

import org.springframework.boot.SpringApplication;

import java.io.File;

import static vn.elca.training.constant.FileConstant.USER_FOLDER;

/**
 * @author vlp
 */
public class ApplicationLauncher {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationWebConfig.class, args);
        new File(USER_FOLDER).mkdirs();
    }
}
