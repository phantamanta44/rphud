# Resource Pack HUD

## Overview
Gives you the ability to modify the HUD using a text file.

## Documentation
Check out documentation on [the Wiki](github.com/phantamanta44/rphud/wiki).

## Examples
![](http://i.imgur.com/H3oGuDe.jpg)
```
- cancels -

health
food

#####
# Health bar
#####
- definitions -

# Fallout 4 default green colour
fo_green = rgb(31, 215, 40)

# Percentage of HP left
hp_percent = hp / hp_max

- components -

text
    align  = bottom-left
    x      = 12
    y      = 12
    text   = HP
    colour = fo_green
end
rect
    align  = bottom-left
    x      = 30
    y      = 16
    width  = 128
    height = 1
    colour = fo_green
end
rect
    align  = bottom-left
    x      = 30
    y      = 17
    width  = 1
    height = 4
    colour = fo_green
end
rect
    align  = bottom-left
    x      = 157
    y      = 17
    width  = 1
    height = 4
    colour = fo_green
end
rect
    align  = bottom-left
    x      = 31
    y      = 17
    width  = 126 * hp_percent
    height = 4
    colour = fo_green
end

#####
# Action point bar
#####
- definitions -

# Percentage of usable sprint bar left
scaled_hunger = max(0, (food - 6) / (food_max - 6))

- components -

text
    align  = bottom-right
    x      = 12
    y      = 12
    text   = AP
    colour = fo_green
end
rect
    align  = bottom-right
    x      = 32
    y      = 16
    width  = 128 * scaled_hunger
    height = 5
    colour = fo_green
end

#####
# Low-health blood splat
#####
img
    underhud = true
    x        = 0
    y        = 0
    width    = vp_width
    height   = vp_height
    source   = rphud:blood.png
    opacity  = 1 - hp_percent
end

#####
# Compass
#####
- definitions -

# Angle relative to north
a2 = (angle + 630) % 360

- components -

img
    align         = bottom
    x             = 0
    y             = 41
    width         = 182
    height        = 12
    u             = 182 * -a2 / 180
    texturewidth  = 364
    source        = rphud:compass.png
end
rect
    align     = bottom
    x         = -91
    y         = 53
    width     = 1
    height    = 4
    colour = fo_green
end
rect
    align     = bottom
    x         = 90
    y         = 53
    width     = 1
    height    = 4
    colour = fo_green
end
```
