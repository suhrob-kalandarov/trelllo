package org.exp.trello.controllers.home;

import jakarta.servlet.http.HttpSession;
import org.exp.trello.models.entities.TaskColumn;
import org.exp.trello.repositories.TaskColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/positions")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
public class PositionController {

    @Autowired
    private TaskColumnRepository taskColumnRepository;

    @GetMapping
    public String showPositionsPage(Model model, HttpSession session) {
        List<TaskColumn> columns = taskColumnRepository.findAllByActiveTrueOrderByPositionAsc();
        model.addAttribute("currentUser", session.getAttribute("user"));
        model.addAttribute("columns", columns);
        return "column/positions";
    }

    @PostMapping("/update")
    public String updatePositions(@RequestParam("columnIds[]") List<Integer> columnIds,
                                  RedirectAttributes redirectAttributes
    ){
        // Update positions based on the order of columnIds
        for (int i = 0; i < columnIds.size(); i++) {
            Optional<TaskColumn> columnOpt = taskColumnRepository.findById(columnIds.get(i));
            if (columnOpt.isPresent()) {
                TaskColumn column = columnOpt.get();
                column.setPosition(i + 1); // Position starts from 1
                taskColumnRepository.save(column);
            }
        }

        redirectAttributes.addFlashAttribute("successMessage", "Column positions updated successfully");
        return "redirect:/positions";
    }
}