/*
 * bowling
 *
 * Copyright (C) 2013 Samjung Data Service Co., Ltd.,
 * Sungsoon Lim <sungsoon0813@sds.co.kr>
 */
package bowling;

public class GameOverException extends Exception{

	@Override
	public String getMessage() {
			return "게임이 종료되었습니다";
	}
	
}
