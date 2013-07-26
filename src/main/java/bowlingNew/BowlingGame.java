/*
 * bowling
 *
 * Copyright (C) 2013 Samjung Data Service Co., Ltd.,
 * Sungsoon Lim <sungsoon0813@sds.co.kr>
 */

package bowlingNew;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BowlingGame {

	// 쓰러진 핀의 수 배열 
	private int[][] hitPin = new int[10][2];

	// 현재 프레임
	private int frame = 0;
	// 해당 프레임에서의 투구 차수 ( 0 or 1 )
	private int frameRoll = 0;

	// 10 프레임의 3번째 투구 여부
	private boolean isBonusRoll = false;

	private int scoreFrameIndex = 0;
	// 점수 계산에 필요한 인덱스를 저장하는 변수
	private int scoreIndex = 0;
	// 투구 수
	private int rollIndex = 0;

	// 점수 총합
	private int totalScore = 0;
	// 출력할 각 투구별 스코어 
	private String printRollScore = "|";
	// 출력할 점수 합계
	private String printTotalScore = "|";


	// 투구 !!!!!!!
	public int roll(int pin) {

		if (pin < 0 || pin > 10)
			return 0;

		if (isTenFrame())
			return tenFrameWork(pin);

		return notTenFrameWork(pin);
	}
	
	

	// 10 프레임 이외에 호출
	private int notTenFrameWork(int pin) {

		if (isGutter(pin)) {
			gutterWork(pin);
			return totalScore;
		}

		if (isStrike(pin)) {
			strikeWork();
			return totalScore;
		}

		if (isSpare(pin)) {
			spareWork(pin);
			return totalScore;
		}


		otherWork(pin);
		return totalScore;
	}


	// Gutter, Strike, Spare 가 아닐 시 작업
	private void otherWork(int pin) {
		
		hitPin[frame][frameRoll] = pin;
		scoreCalculator();
		appendPrintRollScore(pin+"|");
		nextRoll();
		
	}


	// spare 처리 시 작업 
	private void spareWork(int pin) {
		
		hitPin[frame][1] = pin;
		scoreCalculator();
		appendPrintRollScore("/|");
		nextFrame();
		
	}


	// strike 시 작업
	private void strikeWork() {

		hitPin[frame][0] = 10;
		scoreCalculator();
		appendPrintRollScore("X| |");
		nextFrame();

	}


	// gutter 시 작업
	private void gutterWork(int pin) {

		hitPin[frame][frameRoll] = pin;
		scoreCalculator();
		appendPrintRollScore("-|");
		nextRoll();

	}

	// 10 프레임 일 때 호출
	private int tenFrameWork(int pin) {

		// 10 프레임 첫번째 투구
		if (isFirstRollonFrame()) {
			tenFrameFirstRollWork(pin);
			scoreCalculator();
		}

		// 두번째 투구 
		if (frameRoll == 1) {
			tenFrameSecondRollWork(pin);
			scoreCalculator();
		}

		// 세번째 투구
		if (frameRoll == 2) {
			tenFrameThirdRollWork(pin);
			appendPrintTotalScore(toScoreFormatString(totalScore));
		}

		nextFrame();

		return totalScore;
	}

	// 10 프레임 세번째 투구 작업
	private void tenFrameThirdRollWork(int pin) {

		totalScore += hitPin[9][0] + hitPin[9][1] + pin;

		if (pin == 10) {
			appendPrintRollScore("X|");
			return;
		}

		if (pin == 0) {
			appendPrintRollScore("-|");
			return;
		}

		appendPrintRollScore(pin+"|");

	}

	// 10 프레임 두번째 투구 작업
	private void tenFrameSecondRollWork(int pin) {

		hitPin[9][1] = pin;

		// 10프레임의 두번의 투구가 10의 배수이면 보너스 프레임 던질 수 있다
		if ( (double)(hitPin[9][0] + hitPin[9][1]) / 10 == 1) {
			appendPrintRollScore("/|");
			isBonusRoll = true;
			return;
		}

		if ( (double)(hitPin[9][0] + hitPin[9][1]) / 10 == 2) {
			appendPrintRollScore("X|");
			isBonusRoll = true;
			return;
		}

		appendPrintRollScore(hitPin[9][1]+"|");

	}


	// 10 프레임 첫번째 투구 작업
	private void tenFrameFirstRollWork(int pin) {

		hitPin[9][0] = pin;

		if (hitPin[9][0] == 10) {
			appendPrintRollScore("X|");
			isBonusRoll = true;
			return;
		}

		if (hitPin[9][0] == 0) {
			appendPrintRollScore("-|");
			return;
		}

		appendPrintRollScore(hitPin[9][0]+"|");

	}


	// strike, spare가 아닌경우 점수 합
	private void sumHitPinFrame() {

		if (isBonusRoll || hitPin[scoreFrameIndex][0] + hitPin[scoreFrameIndex][1] == 10)
			return;

		totalScore += (hitPin[scoreFrameIndex][0] + hitPin[scoreFrameIndex][1]);
		appendPrintTotalScore(toScoreFormatString(totalScore));
		preNextFrameScore(2);

	}

	// 스트라이크 한번 친 경우 점수 계산
	private void sumStrikeOnce() {
		
		totalScore += (10 + hitPin[scoreFrameIndex+1][0] + hitPin[scoreFrameIndex+1][1]);
		appendPrintTotalScore(toScoreFormatString(totalScore));
		preNextFrameScore(1);
		
	}

	// 스트라이크 두번 연속 친 경우 점수 계산 
	private void sumStrikeDouble() {

		if (scoreFrameIndex == 8)
			totalScore += (10 + hitPin[scoreFrameIndex+1][0] + hitPin[scoreFrameIndex+1][1]);
		else 
			totalScore += (10 + hitPin[scoreFrameIndex+1][0] + hitPin[scoreFrameIndex+2][0]);

		appendPrintTotalScore(toScoreFormatString(totalScore));
		preNextFrameScore(1);
		
	}

	// 스페어 처리시 점수 계산
	private void sumSpare() {
		
		totalScore += (10 + hitPin[scoreFrameIndex+1][0]);
		appendPrintTotalScore(toScoreFormatString(totalScore));
		preNextFrameScore(2);
		
	}

	// 프레임 점수 계산 후 다음 프레임의 점수 계산을 위한 변수 작업
	private void preNextFrameScore(int n) {

		scoreIndex += n;
		scoreFrameIndex++;

	}

	// 점수 계산
	private void scoreCalculator() {

		// 일반 프레임 점수 계산
		if (diffRollIndexScoreIndex(1))
			sumHitPinFrame();

		// 스트라이크, 스페어 계산
		if (diffRollIndexScoreIndex(2)) {

			// 스트라이크
			if ( hitPin[scoreFrameIndex][0] == 10 ) {

				// 더블 스트라이크
				if (hitPin[scoreFrameIndex+1][0] == 10) {
					sumStrikeDouble();
				}

				// 스트라이크
				else {
					sumStrikeOnce();

					// 스페어가 아니면 계산
					if ( !(hitPin[scoreFrameIndex][0] + hitPin[scoreFrameIndex][1] == 10 && hitPin[scoreFrameIndex][0] != 10) ) {
						sumHitPinFrame();
					}
				}
			}

			// 스페어
			else if (hitPin[scoreFrameIndex][0] + hitPin[scoreFrameIndex][1] == 10 && hitPin[scoreFrameIndex][0] != 10) {
				sumSpare();
			}

		}

		rollIndex++;

	}

	// 투구 수 - 점수 계산 인덱스
	private boolean diffRollIndexScoreIndex(int diff) {

		if (rollIndex - scoreIndex == diff)
			return true;

		return false;
	}

	private String toScoreFormatString(int number) {
		return String.format("%3d|", number);
	}

	// 출력할 각 투구별 점수 append
	private void appendPrintRollScore(String hitStr) {
		printRollScore += hitStr;
	}

	// 출력할 점수 합계
	private void appendPrintTotalScore(String scoreStr) {
		printTotalScore += scoreStr;
	}

	// 스트라이크인지 판단
	private boolean isStrike(int pin) {

		// 프레임의 1번째 투구여야 true
		if (!isFirstRollonFrame())
			return false;

		if (pin != 10)
			return false;

		return true;

	}

	// 스페어 처리인지 판단
	private boolean isSpare(int pin) {

		// 프레임의 2번째 투구여야 true
		if (isFirstRollonFrame())
			return false;

		if ( (hitPin[frame][0] + pin) != 10 )
			return false;

		return true;

	}

	// Gutter 인지 판단
	private boolean isGutter(int pin) {

		if (pin == 0)
			return true;

		return false;
	}

	// 다음 프레임으로 진행
	private void nextFrame() {

		// 10 프레임에서는 다음투구로
		if (isTenFrame()) {
			frameRoll++;
			return;
		}

		frame++;
		frameRoll = 0;

	}

	// frame 변수는 배열의 인덱스이므로 (실제프레임 - 1)
	private boolean isTenFrame() {

		if (frame == 9)
			return true;

		return false;
	}

	// 프레임의 첫번쨰 투구이면 두번째 투구로, 두번째 투구이면 다음프레임으로 진행  
	private void nextRoll() {

		if (isFirstRollonFrame()) {
			frameRoll++;
			return;
		}

		nextFrame();

	}

	// 프레임의 첫번째 투구인지 판단
	private boolean isFirstRollonFrame() {

		if (frameRoll == 0)
			return true;

		return false;

	}

	public int inputHitPin() {
		System.out.print((frame+1) + "번 프레임, " + (frameRoll+1) + "번째 투구 : ");

		try {
			Scanner sc = new Scanner(System.in);
			return sc.nextInt();
		} catch (InputMismatchException e) {
			return 0;
		}
	}

	public void isGameOver() throws GameOverException{

		// 10번 프레임이고, 보너스 투구가 없고 frameRoll이 2로 증가하면 종료
		if (frame == 9 && isBonusRoll == false && frameRoll == 2)
			throw new GameOverException();

		// 10번 프레임이고, 보너스 투구가 있고 frameRoll이 3으로 증가하면 종료
		if (frame == 9 && isBonusRoll == true && frameRoll == 3)
			throw new GameOverException();
	}

	public void scoreBoardPrint() {
		System.out.println("--------------------------------------------");
		System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |  10  |");
		System.out.println("--------------------------------------------");
		System.out.println(printRollScore);
		System.out.println("--------------------------------------------");
		System.out.println(printTotalScore);
		System.out.println("--------------------------------------------");
		System.out.println("\n\n\n");
	}

}
