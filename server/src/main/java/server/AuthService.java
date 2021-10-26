package server;

public interface AuthService {
    /**
     * Метод проверки наличия учетной записи
     * @param login, не должен содержать пробелов
     * @param password не должен содержать пробелов
     * @return nickname, если она существует, в противном случае null
     */
    String getNicknameByLoginAndPassword(String login, String password);

    /**
     * Метод для регистрации новой учетной записи
     * @param login
     * @param password
     * @param nickname
     * @return true, если регистрация прошла успешно
     * @return false, если логин или никнейм уже заняты
     */
    boolean registration(String login, String password, String nickname);

    boolean changeNick(String oldNickname, String newNickname);
}
