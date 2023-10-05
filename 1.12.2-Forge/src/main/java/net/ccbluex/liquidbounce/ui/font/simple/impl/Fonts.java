package net.ccbluex.liquidbounce.ui.font.simple.impl;


import net.ccbluex.liquidbounce.ui.font.simple.api.FontFamily;
import net.ccbluex.liquidbounce.ui.font.simple.api.FontManager;
import net.ccbluex.liquidbounce.ui.font.simple.api.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.simple.api.FontType;
import net.ccbluex.liquidbounce.utils.ClientMain;

/**
 * @author Artyom Popov
 * @since June 30, 2020
 */
@SuppressWarnings("SpellCheckingInspection")
public interface Fonts {

	FontManager FONT_MANAGER = ClientMain.getInstance().getFontManager();

	interface JelloMedium {
		FontFamily JelloMedium = FONT_MANAGER.fontFamily(FontType.JelloMedium);
		final class JelloMedium_16 { public static final FontRenderer JelloMedium_16 = JelloMedium.ofSize(16); private JelloMedium_16() {} }
		final class JelloMedium_28 { public static final FontRenderer JelloMedium_28 = JelloMedium.ofSize(20); private JelloMedium_28() {} }
		final class JelloMedium_22 { public static final FontRenderer JelloMedium_22 = JelloMedium.ofSize(24); private JelloMedium_22() {} }
		final class JelloMedium_32 { public static final FontRenderer JelloMedium_32 = JelloMedium.ofSize(32); private JelloMedium_32() {} }
		final class JelloMedium_35 { public static final FontRenderer JelloMedium_35 = JelloMedium.ofSize(35); private JelloMedium_35() {} }
		final class JelloMedium_50 { public static final FontRenderer JelloMedium_50 = JelloMedium.ofSize(50); private JelloMedium_50() {} }
	}
	interface JelloLight {
		FontFamily JelloMedium = FONT_MANAGER.fontFamily(FontType.JelloLight);
		final class JelloLight_24 { public static final FontRenderer JelloLight_24 = JelloMedium.ofSize(24); private JelloLight_24() {} }
	}

    interface ICONFONT {

		FontFamily ICONFONT = FONT_MANAGER.fontFamily(FontType.ICONFONT);

		final class ICONFONT_16 { public static final FontRenderer ICONFONT_16 = ICONFONT.ofSize(16); private ICONFONT_16() {} }
		final class ICONFONT_20 { public static final FontRenderer ICONFONT_20 = ICONFONT.ofSize(20); private ICONFONT_20() {} }
		final class ICONFONT_24 { public static final FontRenderer ICONFONT_24 = ICONFONT.ofSize(24); private ICONFONT_24() {} }
		final class ICONFONT_32 { public static final FontRenderer ICONFONT_32 = ICONFONT.ofSize(32); private ICONFONT_32() {} }
		final class ICONFONT_35 { public static final FontRenderer ICONFONT_35 = ICONFONT.ofSize(35); private ICONFONT_35() {} }
		final class ICONFONT_50 { public static final FontRenderer ICONFONT_50 = ICONFONT.ofSize(50); private ICONFONT_50() {} }
	}

	interface CheckFont {

		FontFamily CheckFont = FONT_MANAGER.fontFamily(FontType.Check);

		final class CheckFont_16 { public static final FontRenderer CheckFont_16 = CheckFont.ofSize(16); private CheckFont_16() {} }
		final class CheckFont_20 { public static final FontRenderer CheckFont_20 = CheckFont.ofSize(20); private CheckFont_20() {} }
		final class CheckFont_24 { public static final FontRenderer CheckFont_24 = CheckFont.ofSize(24); private CheckFont_24() {} }
		final class CheckFont_32 { public static final FontRenderer CheckFont_32 = CheckFont.ofSize(32); private CheckFont_32() {} }
		final class CheckFont_35 { public static final FontRenderer CheckFont_35 = CheckFont.ofSize(35); private CheckFont_35() {} }
		final class CheckFont_50 { public static final FontRenderer CheckFont_50 = CheckFont.ofSize(50); private CheckFont_50() {} }
	}
	interface SF {

		FontFamily SF = FONT_MANAGER.fontFamily(FontType.SF);
		final class SF_9 { public static final FontRenderer SF_9 = SF.ofSize(9); private SF_9() {} }
		final class SF_11 { public static final FontRenderer SF_11 = SF.ofSize(11); private SF_11() {} }
		final class SF_14 { public static final FontRenderer SF_14 = SF.ofSize(14); private SF_14() {} }
		final class SF_15 { public static final FontRenderer SF_15 = SF.ofSize(15); private SF_15() {} }
		final class SF_16 { public static final FontRenderer SF_16 = SF.ofSize(16); private SF_16() {} }
		final class SF_17 { public static final FontRenderer SF_17 = SF.ofSize(17); private SF_17() {} }
		final class SF_18 { public static final FontRenderer SF_18 = SF.ofSize(18); private SF_18() {} }
		final class SF_19 { public static final FontRenderer SF_19 = SF.ofSize(19); private SF_19() {} }
		final class SF_20 { public static final FontRenderer SF_20 = SF.ofSize(20); private SF_20() {} }
		final class SF_21 { public static final FontRenderer SF_21 = SF.ofSize(21); private SF_21() {} }
		final class SF_22 { public static final FontRenderer SF_22 = SF.ofSize(22); private SF_22() {} }
		final class SF_23 { public static final FontRenderer SF_23 = SF.ofSize(23); private SF_23() {} }
		final class SF_24 { public static final FontRenderer SF_24 = SF.ofSize(24); private SF_24() {} }
		final class SF_25 { public static final FontRenderer SF_25 = SF.ofSize(25); private SF_25() {} }
		final class SF_26 { public static final FontRenderer SF_26 = SF.ofSize(26); private SF_26() {} }
		final class SF_27 { public static final FontRenderer SF_27 = SF.ofSize(27); private SF_27() {} }
		final class SF_28 { public static final FontRenderer SF_28 = SF.ofSize(28); private SF_28() {} }
		final class SF_29 { public static final FontRenderer SF_29 = SF.ofSize(29); private SF_29() {} }
		final class SF_30 { public static final FontRenderer SF_30 = SF.ofSize(30); private SF_30() {} }
		final class SF_31 { public static final FontRenderer SF_31 = SF.ofSize(31); private SF_31() {} }
		final class SF_50 { public static final FontRenderer SF_50 = SF.ofSize(45); private SF_50() {} }
	}




	interface CsgoIcon {

		FontFamily csgoicon = FONT_MANAGER.fontFamily(FontType.csgoicon);
		final class csgoicon_18 { public static final FontRenderer csgoicon_18 = csgoicon.ofSize(18); private csgoicon_18() {} }
		final class csgoicon_20 { public static final FontRenderer csgoicon_20 = csgoicon.ofSize(20); private csgoicon_20() {} }
		final class csgoicon_24 { public static final FontRenderer csgoicon_24 = csgoicon.ofSize(24); private csgoicon_24() {} }
		final class csgoicon_32 { public static final FontRenderer csgoicon_32 = csgoicon.ofSize(32); private csgoicon_32() {} }
		final class csgoicon_35 { public static final FontRenderer csgoicon_35 = csgoicon.ofSize(35); private csgoicon_35() {} }
		final class csgoicon_40 { public static final FontRenderer csgoicon_40 = csgoicon.ofSize(40); private csgoicon_40() {} }
		final class csgoicon_55 { public static final FontRenderer csgoicon_55 = csgoicon.ofSize(55); private csgoicon_55() {} }
		//final class csgoicon_60 { public static final FontRenderer csgoicon_60 = csgoicon.ofSize(60); private csgoicon_60() {} }

	}
	interface Tahoma {

		FontFamily Tahoma = FONT_MANAGER.fontFamily(FontType.Tahoma);

		final class Tahoma_11 { public static final FontRenderer Tahoma_11 = Tahoma.ofSize(11); private Tahoma_11() {} }
		final class Tahoma_12 { public static final FontRenderer Tahoma_12 = Tahoma.ofSize(12); private Tahoma_12() {} }
		final class Tahoma_14 { public static final FontRenderer Tahoma_14 = Tahoma.ofSize(14); private Tahoma_14() {} }
		final class Tahoma_16 { public static final FontRenderer Tahoma_16 = Tahoma.ofSize(16); private Tahoma_16() {} }
		final class Tahoma_18 { public static final FontRenderer Tahoma_18 = Tahoma.ofSize(18); private Tahoma_18() {} }
		final class Tahoma_20 { public static final FontRenderer Tahoma_20 = Tahoma.ofSize(20); private Tahoma_20() {} }
		final class Tahoma_22 { public static final FontRenderer Tahoma_22 = Tahoma.ofSize(22); private Tahoma_22() {} }
		final class Tahoma_26 { public static final FontRenderer Tahoma_26 = Tahoma.ofSize(26); private Tahoma_26() {} }
		final class Tahoma_28 { public static final FontRenderer Tahoma_28 = Tahoma.ofSize(28); private Tahoma_28() {} }
		final class Tahoma_35 { public static final FontRenderer Tahoma_35 = Tahoma.ofSize(35); private Tahoma_35() {} }

	}
	interface SFBOLD {

		FontFamily SFBOLD = FONT_MANAGER.fontFamily(FontType.SFBOLD);

		final class SFBOLD_14 { public static final FontRenderer SFBOLD_14 = SFBOLD.ofSize(11); private SFBOLD_14() {} }
		final class SFBOLD_15 { public static final FontRenderer SFBOLD_15 = SFBOLD.ofSize(12); private SFBOLD_15() {} }
		final class SFBOLD_16 { public static final FontRenderer SFBOLD_16 = SFBOLD.ofSize(16); private SFBOLD_16() {} }
		final class SFBOLD_18 { public static final FontRenderer SFBOLD_18 = SFBOLD.ofSize(18); private SFBOLD_18() {} }
		final class SFBOLD_20 { public static final FontRenderer SFBOLD_20 = SFBOLD.ofSize(20); private SFBOLD_20() {} }
		final class SFBOLD_22 { public static final FontRenderer SFBOLD_22 = SFBOLD.ofSize(22); private SFBOLD_22() {} }
		final class SFBOLD_26 { public static final FontRenderer SFBOLD_26 = SFBOLD.ofSize(26); private SFBOLD_26() {} }
		final class SFBOLD_28 { public static final FontRenderer SFBOLD_28 = SFBOLD.ofSize(28); private SFBOLD_28() {} }
		final class SFBOLD_35 { public static final FontRenderer SFBOLD_35 = SFBOLD.ofSize(35); private SFBOLD_35() {} }
	}


}
