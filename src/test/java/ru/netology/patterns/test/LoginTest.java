package ru.netology.patterns.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.patterns.data.DataGenerator.*;
import static ru.netology.patterns.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.patterns.data.DataGenerator.Registration.getUser;

public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Successful login by active registered user")
    void shouldSuccessfullyLogin() {
        var registeredUser = getRegisteredUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$(".button").click();
        $(".heading").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Attempt to login a registered but blocked user")
    void shouldGetErrorIfUserStatusBlocked() {
        var registeredUser = getRegisteredUser("blocked");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$(".button").click();
        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Attempt to login to unregistered user")
    void shouldGetErrorIfUserUnregistered() {
        var notRegisteredUser = getUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        form.$(".button").click();
        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Attempt to login with incorrect password")
    void shouldGetErrorIfIncorrectPassword() {
        var registeredUser = getRegisteredUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(generatePassword());
        form.$(".button").click();
        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Attempt to login with incorrect login")
    void shouldGetErrorIfIncorrectLogin() {
        var registeredUser = getRegisteredUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(generateLogin());
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$(".button").click();
        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
