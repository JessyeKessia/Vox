package br.edu.ifpb.pweb2.vox.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private boolean isPublicPath(String path) {
        return path.startsWith("/auth") || 
        path.equals("/") ||  
        path.startsWith("/css") || 
        path.startsWith("/js") || 
        path.startsWith("/images") || 
        path.startsWith("/error");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        // url requisitada
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        // caminhos públicos
        if (isPublicPath(path)) {
            return true;
        }
         // Se estiver logado, permite acessar a home
        if (usuario != null) {
            if (path.equals("/home") || path.equals("/")) {
                return true;
            }
        }
        
        // bloqueia se não tiver logado
        if (usuario == null && !uri.contains(contextPath + "/auth")) {
            response.sendRedirect(contextPath + "/auth");
            return false;
        }

        // controle por role
        if (usuario != null) {
            // pega a role do usuário logado
            Role role = ((Usuario) usuario).getRole(); 

            switch (role) {
                case ADMIN:
                    // ADMIN só pode acessar rotas de administração
                    if (!(uri.startsWith(contextPath + "/alunos") ||
                        uri.startsWith(contextPath + "/assuntos") ||
                        uri.startsWith(contextPath + "/colegiado") ||
                        uri.startsWith(contextPath + "/professor"))) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado para ADMIN");
                        return false;
                    }
                    break;

                case ALUNO:
                    // ALUNO só pode acessar /processo
                    if (!(uri.startsWith(contextPath + "/processo"))) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado para ALUNO");
                        return false;
                    }
                    // Bloqueia ALUNO de acessar rota de professores
                    if (uri.startsWith(contextPath + "/processos/professores")) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "ALUNO não pode acessar esta rota");
                        return false;
                    }
                    break;

                case PROFESSOR:
                    // PROFESSOR só pode acessar /processos/professores
                    if (!uri.startsWith(contextPath + "/processos/professores")) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado para PROFESSOR");
                        return false;
                    }
                    break;

                case COORDENADOR:
                    // COORDENADOR só pode acessar /coordenadores/processos
                    if (!uri.startsWith(contextPath + "/coordenadores/processos")) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado para COORDENADOR");
                        return false;
                    }
                    break;

                default:
                    // Qualquer role desconhecida não tem acesso
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado");
                    return false;
        }
    }

    return true; // se passou por todas as validações, permite a requisição
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // Sem uso
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Sem uso
    }
}
