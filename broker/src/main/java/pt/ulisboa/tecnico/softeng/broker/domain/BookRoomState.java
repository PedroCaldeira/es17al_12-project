package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface.RoomType;

public class BookRoomState extends BookRoomState_Base {
	public static final int MAX_REMOTE_ERRORS = 10;

	@Override
	public State getValue() {
		return State.BOOK_ROOM;
	}

	@Override
	public void process() {
		try {
			getAdventure().setRoomConfirmation(
					HotelInterface.reserveRoom(RoomType.SINGLE, getAdventure().getBegin(), getAdventure().getEnd()));
		} catch (HotelException he) {
			getAdventure().setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				getAdventure().setState(State.UNDO);
			}
			return;
		}

		getAdventure().setState(State.CONFIRMED);
	}

}
