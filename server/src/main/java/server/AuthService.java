package server;

public interface AuthService {
    /**
     * Метод проверки наличия учетной записи
     * @param login, не должен содержать пробелов
     * @param password не должен содержать пробелов
     * @return nickname, если она существует, в противном случае null
     */
    String getNicknameByLoginAndPassword(String login, String password);
}
