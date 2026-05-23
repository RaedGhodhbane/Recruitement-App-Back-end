package com.app.recruitmentapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RecruitmentAppApplication Tests")
class RecruitmentAppApplicationTests {

    @Nested
    @DisplayName("Application Class Structure Tests")
    class ApplicationClassStructureTests {

        @Test
        @DisplayName("Should have SpringBootApplication annotation")
        void shouldHaveSpringBootApplicationAnnotation() {
            assertTrue(RecruitmentAppApplication.class.isAnnotationPresent(SpringBootApplication.class));
        }

        @Test
        @DisplayName("Should have EnableScheduling annotation")
        void shouldHaveEnableSchedulingAnnotation() {
            assertTrue(RecruitmentAppApplication.class.isAnnotationPresent(EnableScheduling.class));
        }

        @Test
        @DisplayName("Should have main method with correct signature")
        void shouldHaveMainMethod() throws NoSuchMethodException {
            Method mainMethod = RecruitmentAppApplication.class.getMethod("main", String[].class);
            assertThat(mainMethod.getReturnType()).isEqualTo(Void.TYPE);
        }

        @Test
        @DisplayName("Should be instantiable")
        void shouldBeInstantiable() {
            assertDoesNotThrow(RecruitmentAppApplication::new);
        }
    }

    @Nested
    @DisplayName("SpringBootApplication Annotation Tests")
    class SpringBootApplicationAnnotationTests {

        @Test
        @DisplayName("Should scan correct base package")
        void shouldScanCorrectBasePackage() {
            SpringBootApplication annotation = RecruitmentAppApplication.class
                    .getAnnotation(SpringBootApplication.class);
            assertArrayEquals(new String[]{"com.app.recruitmentapp"}, annotation.scanBasePackages());
        }
    }
}
