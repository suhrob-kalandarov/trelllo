package org.exp.trello.controllers.home;

import org.exp.trello.models.entities.TaskColumn;
import org.exp.trello.repositories.TaskColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/column")
public class TaskColumnController {

    @Autowired
    private TaskColumnRepository taskColumnRepository;

    @PostMapping("/create")
    public String createColumn(@RequestParam String name,
                               @RequestParam(name = "active", defaultValue = "false") boolean active,
                               RedirectAttributes redirectAttributes
    ) {
        // Get the highest position value
        Integer maxPosition = taskColumnRepository.findMaxPosition();
        int newPosition = (maxPosition != null) ? maxPosition + 1 : 1;

        // Create new column
        TaskColumn column = new TaskColumn();
        column.setName(name);
        column.setInActive(!active);
        column.setPosition(newPosition);

        taskColumnRepository.save(column);

        redirectAttributes.addFlashAttribute("successMessage", "Column created successfully");
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteColumn(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        taskColumnRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Column deleted successfully");
        return "redirect:/";
    }
}