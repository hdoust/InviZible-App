package pan.alexander.tordnscrypt.itpd_fragment;

/*
    This file is part of InviZible Pro.

    InviZible Pro is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    InviZible Pro is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with InviZible Pro.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2019-2022 by Garmatin Oleksandr invizible.soft@gmail.com
*/

public interface ITPDFragmentPresenterInterface {
    boolean isITPDInstalled();

    void displayLog();

    void stopDisplayLog();

    void refreshITPDState();

    void setITPDSomethingWrong();

    void setITPDStopped();

    void setITPDInstalling();

    void setITPDInstalled();

    void setITPDStartButtonEnabled(boolean enabled);

    void setITPDProgressBarIndeterminate(boolean indeterminate);
}
