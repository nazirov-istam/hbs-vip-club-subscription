package org.example.grand_education.application;

import org.springframework.stereotype.Component;

@Component
public class Messages {
    public static final String callBackStart = "DELETE_VIDEO_";
    public static final String deleteSuccessMessage = "Video muvaffaqiyatli o‘chirildi. ✅";
    public static final String delete = "❌ O‘chirish";
    public static final String finish = "📍 Yakunlash";
    public static final String deleteAllVideosSuccess = "🗑 Barcha videolar muvaffaqiyatli o‘chirildi. ✅";
    public static final String deleteSectionMessage = "🗑 Siz 'Videolarni o‘chirish' bo‘limidasiz.";
    public static final String deleteVideo = "🗑 Videoni o‘chirish";
    public static final String failChangeRole = "⚠️ Kechirasiz, bu amalni bajarish uchun sizda yetarli huquq mavjud emas.";
    public static final String changeRoleToAdmin = "👤 Administrator roliga o‘tish";
    public static final String changeRoleToManager = "🔄 'Menejer' roliga o‘tish";
    public static final String noCommandPrompt = "⚠️ Bunday buyruq mavjud emas.";

    public static final String channelText = """
            🎉 Grand University botiga xush kelibsiz!
            
            📢 Davom etish uchun quyidagi kanalga obuna bo‘ling:
            00001 ✅ Obunani tekshirish ✅
            """;

    public static final String channelTextAgain = """
            📢 Davom etish uchun, iltimos, quyidagi kanalga obuna bo‘ling:
            00001 ✅ Obunani tekshirish ✅
            """;

    public static final String success = """
            🎉 Siz muvaffaqiyatli ro‘yxatdan o‘tdingiz!
            
            📚 Endi zamonaviy va bepul video darsliklar orqali kasb o‘rganishingiz mumkin.
            🚀 Grand University – Sizning kelajagingiz shu yerdan boshlanadi!

            💡 BEPUL video darsliklarni olish uchun, pastdagi tugmani bosing.
            """;


    public static final String confirmationMessageForSetting = """
            🗒 Yangi kanal ma’lumotlari:
            
            🔠 Nomi: %s
            🔗 Havola: %s
            🆔 ID: %s
            
            ✅ Tasdiqlaysizmi?
            """;

    public static final String userAlreadyExist = "ℹ️ Siz avval ro‘yxatdan o‘tgan ekansiz.";
    public static final String adminSuccess = "👤 Siz botdan administrator sifatida foydalanmoqdasiz.";
    public static final String managerSuccess = "👥 Siz botdan menejer sifatida foydalanmoqdasiz.";

    public static final String addAdmin = "➕ Administrator qo‘shish";
    public static final String deleteAdmin = "🗑 Administratorni o‘chirish";
    public static final String adminSettingsSection = "⚙️ Siz hozir kanal sozlamalari bo‘limidasiz.";
    public static final String adminMainMenu = "🏠 Siz hozir asosiy menyudasiz.";
    public static final String settingsSection = "🔐 Kanal sozlamalari";
    public static final String statsSection = "📊 Statistika";

    public static final String back = "⬅️ Orqaga";
    public static final String addChannel = "🆕 Kanal qo‘shish";
    public static final String deleteChannel = "🗑 Kanalni o‘chirish";
    public static final String allChannels = "📋 Barcha kanallar";
    public static final String addVideo = "➕ Video qo‘shish";
    public static final String addVideoMessage = "📹 Iltimos, videoni yuboring:";
    public static final String viewAllVideos = "📂 Barcha videolar";
    public static final String getAllVideoLessons = "🎥 Video darsliklar";

    public static final String addAdminChatId = "🆔 Administrator chat ID raqamini kiriting:";
    public static final String addAdminName = "👤 Administrator ismini kiriting:";
    public static final String deleteAdminMessage = "🗑 O‘chirmoqchi bo‘lgan administratorni tanlang:";
    public static final String addAdminSuccess = "✅ Yangi administrator muvaffaqiyatli qo‘shildi.";
    public static final String deleteAdminSuccess = "✅ Administrator muvaffaqiyatli o‘chirildi.";
    public static final String deleteAdminNo = "❌ Administratorlar mavjud emas.";

    public static final String deleteChannelNo = "❌ Kanallar mavjud emas.";
    public static final String noUserMessage = "❌ Bot foydalanuvchilari mavjud emas.";

    public static final String addChannelName = "🔠 Kanal nomini kiriting:";
    public static final String addChannelUrl = "🔗 Kanal havolasini kiriting:";
    public static final String addChannelId = "🆔 Kanal ID raqamini kiriting:";
    public static final String addChannelSuccess = "✅ Kanal muvaffaqiyatli qo‘shildi.";
    public static final String addChannelDecline = "❌ Kanal saqlanmadi.";

    public static final String deleteChannelMessage = "🗑 O‘chirmoqchi bo‘lgan kanalni tanlang:";
    public static final String deleteChannelSuccess = "✅ Kanal muvaffaqiyatli o‘chirildi.";

    public static final String addVideoSuccess = "✅ Video muvaffaqiyatli saqlandi.";
    public static final String confirmMessageUz = "✅ Tasdiqlash";
    public static final String declineMessageUz = "❌ Bekor qilish";

    public static final String deleteAllVideos = "🗑 Barcha videolarni o‘chirish";
    public static final String deleteSeparately = "🗑 Alohida videoni o‘chirish";
    public static final String noVideos = "📍 Hozircha hech qanday video mavjud emas.";
}
