/*
 * bowling
 *
 * Copyright (C) 2013 Samjung Data Service Co., Ltd.,
 * Sungsoon Lim <sungsoon0813@sds.co.kr>
 */

package bowlingNew;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BowlingTest {

	private BowlingGame game;


	@Before
	public void setup() {
		game = new BowlingGame();
	}

//	// pin으로 0보다 작은 값이 입력 되었을 때
//	@Test
//	public void underPin() {
//		game.roll(-1);
//	}
//
//	// pin으로 10보다 큰 값이 입력 되었을 때
//	@Test
//	public void overPin() {
//		game.roll(11);
//	}
//
//	// Gutter 테스트
//	@Test
//	public void gutter() {
//		game.roll(0);
//		int total = game.roll(0);
//		assertThat(total, is(0));
//	}
//
//	// 모든 투구 Gutter 테스트
//	@Test
//	public void gutterAll() {
//
//		int total = 0;
//
//		try {
//
//			while (true) {
//				game.roll(0);
//				game.isGameOver();
//			}
//
//		} catch (GameOverException e) {
//			assertThat(total, is(0));
//		}
//
//	}
//
//	// perfectGame 테스트
//	@Test
//	public void perfectGame() {
//
//		System.out.println("perfrectGame()");
//
//		int total = 0;
//
//		try {
//
//			while (true) {
//				total = game.roll(10);
//				game.isGameOver();
//			}
//
//		} catch (GameOverException e) {
//			game.scoreBoardPrint();
//			assertThat(total, is(300));
//		}
//
//	}
//
//	// 모든 투구 1점 테스트
//	@Test
//	public void allOne() {
//
//		System.out.println("allOne()");
//
//		int total = 0;
//
//		try {
//
//			while (true) {
//				total = game.roll(1);
//				game.isGameOver();
//			}
//
//		} catch (GameOverException e) {
//			game.scoreBoardPrint();
//			assertThat(total, is(20));
//		}
//	}


	// pin을 입력 받으며 점수 계산 테스트
	@Test
	public void bowlingGame() {

		System.out.println("bowlingGame()");

		int pin = 0;

		try {

			while (true) {
				pin = game.inputHitPin();
				game.roll(pin);
				game.scoreBoardPrint();
				game.isGameOver();
			}

		} catch (GameOverException e) {
			e.printStackTrace();
		}

	}

}
