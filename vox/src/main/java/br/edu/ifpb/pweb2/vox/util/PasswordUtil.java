package br.edu.ifpb.pweb2.vox.util;

import org.mindrot.jbcrypt.BCrypt;

public abstract class PasswordUtil {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            return true;
        else
            return false;
    }

    public static void main(String[] args) {
        String senha = "123";
        System.out.println(PasswordUtil.hashPassword(senha));
    }

}
